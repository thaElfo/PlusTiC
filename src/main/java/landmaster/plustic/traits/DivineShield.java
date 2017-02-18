package landmaster.plustic.traits;

import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class DivineShield extends AbstractTrait {
	public static final DivineShield divineShield = new DivineShield();
	
	public DivineShield() {
		super("divineshield", 0x00FFFF);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (isSelected && entity instanceof EntityLiving) {
			((EntityLiving)entity).addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void defend(LivingHurtEvent event) {
		if (event.getEntity().getEntityWorld().isRemote
				|| event.isCanceled()
				|| !TinkerUtil.hasTrait(
						TagUtil.getTagSafe(event.getEntityLiving().getHeldItemMainhand()),
						getIdentifier())) return;
		event.setAmount(event.getAmount() * 0.85f);
		ToolHelper.damageTool(event.getEntityLiving().getHeldItemMainhand(), 1, event.getEntityLiving());
	}
}
