package landmaster.plustic.traits;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import slimeknights.tconstruct.library.traits.*;

public class Terrafirma extends AbstractTrait {
	public static final Terrafirma terrafirma = new Terrafirma();
	
	public Terrafirma() {
		super("terrafirma",0x00FF00);
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (entity instanceof EntityLivingBase && random.nextFloat() < 0.01) {
			((EntityLivingBase)entity).heal(1);
		}
	}
}
