package landmaster.plustic.traits;

import java.util.*;
import java.util.function.*;

import landmaster.plustic.api.*;
import landmaster.plustic.util.*;
import mcjty.lib.tools.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.items.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

/**
 * Abstract class for PlusTiC traits that rescue the holder from death given certain items.
 * @author Landmaster
 *
 */
public abstract class DeathSaveTrait extends AbstractTrait {
	private final int cost;
	private final Predicate<ItemStack> stackMatcher;
	private final String unlocSaveMessage;
	
	public DeathSaveTrait(String identifier, int color, int cost, Predicate<ItemStack> stackMatcher, String unlocSaveMessage) {
		super(identifier, color);
		this.cost = cost;
		this.stackMatcher = stackMatcher;
		this.unlocSaveMessage = unlocSaveMessage;
		MinecraftForge.EVENT_BUS.register(this);
		Toggle.toggleable.add(identifier);
		Portal.portalable.add(identifier);
	}
	
	@Override
	public String getLocalizedDesc() {
		// add the item cost to the description
		return String.format(super.getLocalizedDesc(), cost);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void timing(LivingHurtEvent event) {
		if (event.getEntity().getEntityWorld().isRemote
				|| !(event.getEntity() instanceof EntityPlayerMP)
				|| event.getEntityLiving().getHealth() > event.getAmount()) {
			return;
		}
		
		Arrays.stream(EnumHand.values())
		.map(event.getEntityLiving()::getHeldItem)
		.map(TagUtil::getTagSafe)
		.filter(nbt -> TinkerUtil.hasTrait(nbt, identifier)
				&& Toggle.getToggleState(nbt, identifier)
				&& nbt.hasKey(Portal.PORTAL_NBT, 10))
		.map(nbt -> nbt.getCompoundTag(Portal.PORTAL_NBT))
		.map(Coord4D::fromNBT)
		.filter(coord -> Utils.canTeleportTo((EntityPlayer)event.getEntity(), coord))
		.findFirst().ifPresent(coord -> {
			IItemHandler ih = event.getEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for (int i=0; i<ih.getSlots(); ++i) {
				ItemStack is = ih.extractItem(i, cost, true);
				if (stackMatcher.test(is) && ItemStackTools.getStackSize(is) >= cost) {
					Utils.teleportPlayerTo((EntityPlayerMP)event.getEntity(), coord);
					ih.extractItem(i, cost, false);
					event.setCanceled(true);
					event.getEntityLiving().clearActivePotions();
					event.getEntityLiving().extinguish();
					event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 160));
					ChatTools.addChatMessage(event.getEntity(), new TextComponentTranslation(
							unlocSaveMessage));
					return;
				}
			}
		});
	}
}
