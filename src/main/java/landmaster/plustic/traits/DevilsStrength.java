package landmaster.plustic.traits;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import slimeknights.tconstruct.library.traits.*;

public class DevilsStrength extends AbstractTrait {
	public static final DevilsStrength devilsstrength = new DevilsStrength();
	
	public DevilsStrength() {
		super("devilsstrength", 0xFF0000);
	}
	
	@Override
	public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
		if (player.dimension != 0) {
			newDamage += 2;
		}
		return super.damage(tool, player, target, damage, newDamage, isCritical);
	}
}
