package landmaster.plustic.traits;

import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import slimeknights.tconstruct.library.traits.*;

public class Apocalypse extends AbstractTrait {
	public static final Apocalypse apocalypse = new Apocalypse();
	
	public Apocalypse() {
		super("apocalypse",0x3A2D7D);
	}
	
	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		if (wasHit && target.isEntityAlive()) {
			float rnd = random.nextFloat();
			if (rnd < 0.05) {
				target.addPotionEffect(new PotionEffect(MobEffects.WITHER,(int)(100+rnd*2500),3));
			} else if (rnd < 0.2) {
				target.addPotionEffect(new PotionEffect(MobEffects.WITHER,(int)(100+rnd*500),2));
			} else {
				target.addPotionEffect(new PotionEffect(MobEffects.WITHER,130));
			}
		}
	}
}
