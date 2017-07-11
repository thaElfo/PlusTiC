package landmaster.plustic.traits;

import landmaster.plustic.api.*;
import landmaster.plustic.util.*;
import mcjty.lib.tools.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.util.text.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.items.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class NickOfTime extends AbstractTrait {
	public static final NickOfTime nickOfTime = new NickOfTime();
	
	public static final int ENDER_COST = 8;
	
	public NickOfTime() {
		super("nickoftime", 0xFFF98E);
		MinecraftForge.EVENT_BUS.register(this);
		Toggle.toggleable.add(identifier);
		Portal.portalable.add(identifier);
	}
	
	@Override
	public String getLocalizedDesc() {
		// add the ender pearl cost to the description
		return String.format(super.getLocalizedDesc(), ENDER_COST);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void timing(LivingHurtEvent event) {
		NBTTagCompound nbt = TagUtil.getTagSafe(event.getEntityLiving().getHeldItemMainhand());
		Coord4D coord;
		if (!event.getEntity().getEntityWorld().isRemote
				&& event.getEntity() instanceof EntityPlayerMP
				&& event.getEntityLiving().getHealth() <= event.getAmount()
				&& TinkerUtil.hasTrait(nbt, identifier)
				&& Toggle.getToggleState(nbt, identifier)
				&& nbt.hasKey(Portal.PORTAL_NBT, 10)
				&& Utils.canTeleportTo((EntityPlayer)event.getEntity(),
						(coord = Coord4D.fromNBT(nbt.getCompoundTag(Portal.PORTAL_NBT))))) {
			IItemHandler ih = event.getEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for (int i=0; i<ih.getSlots(); ++i) {
				ItemStack is = ih.extractItem(i, ENDER_COST, true);
				if (is != null && is.getItem() == Items.ENDER_PEARL && ItemStackTools.getStackSize(is) >= ENDER_COST) {
					Utils.teleportPlayerTo((EntityPlayerMP)event.getEntity(), coord);
					ih.extractItem(i, ENDER_COST, false);
					event.setCanceled(true);
					event.getEntityLiving().clearActivePotions();
					event.getEntityLiving().extinguish();
					event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 160));
					ChatTools.addChatMessage(event.getEntity(), new TextComponentTranslation(
							"msg.plustic.nickmodifier.use"));
					return;
				}
			}
		}
	}
}
