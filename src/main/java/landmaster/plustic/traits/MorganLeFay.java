package landmaster.plustic.traits;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import slimeknights.tconstruct.library.traits.*;

public class MorganLeFay extends AbstractTrait {
	public static final MorganLeFay morganlefay = new MorganLeFay();
	
	public MorganLeFay() {
		super("morganlefay", 0xFF00FF);
	}
	
	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		if (wasHit && player.getEntityWorld().provider.getDimension() != 0) {
			target.hurtResistantTime = 0;
			target.lastDamage = 0;
			target.attackEntityFrom(new EntityDamageSource("morganlefay", player)
					.setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage(),
					(float)(random.nextGaussian()*5));
		}
	}
}
