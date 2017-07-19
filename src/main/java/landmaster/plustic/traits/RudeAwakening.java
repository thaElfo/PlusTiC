package landmaster.plustic.traits;

import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class RudeAwakening extends AbstractTrait {
	public static final RudeAwakening rudeawakening = new RudeAwakening();
	
	public RudeAwakening() {
		super("rudeawakening", 0xFFB200);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST/* for now */)
	public void attack(LivingHurtEvent event) {
		if (event.getSource() instanceof EntityDamageSource && event.getEntity() instanceof IMob) {
			Entity ent = event.getSource().getTrueSource();
			if (ent instanceof EntityLivingBase) {
				ItemStack tool = ((EntityLivingBase)ent).getHeldItemMainhand();
				if (TinkerUtil.hasTrait(TagUtil.getTagSafe(tool), identifier)) {
					event.getSource().setDamageBypassesArmor();
				}
			}
		}
	}
}
