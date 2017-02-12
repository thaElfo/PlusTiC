package landmaster.plustic.traits;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import slimeknights.tconstruct.library.traits.*;

public class Terrafirma extends AbstractTraitLeveled {
	public static final Terrafirma[] terrafirma = new Terrafirma[] {
			new Terrafirma(1),
			new Terrafirma(2)
	};
	
	public Terrafirma(int levels) {
		super("terrafirma", 0x00FF00, 3, levels);
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (isSelected && entity instanceof EntityLivingBase && random.nextFloat() < 0.01) {
			((EntityLivingBase)entity).heal(levels/3.0f);
		}
	}
}
