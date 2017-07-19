package landmaster.plustic.traits;

import landmaster.plustic.api.*;
import landmaster.plustic.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class Psicological extends AbstractTrait {
	public static final int PSI_COST = 28;
	
	public static final Psicological psicological = new Psicological();
	
	public Psicological() {
		super("psicological", 0x6D9EFF);
		Toggle.toggleable.add(identifier);
	}
	
	@Override
	public boolean canApplyTogether(IToolMod otherModifier) {
		return super.canApplyTogether(otherModifier) /*&& !otherModifier.getIdentifier().equals(Mana.mana.getIdentifier())*/;
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (entity instanceof EntityPlayer
				&& Toggle.getToggleState(tool, identifier)
				&& ToolHelper.getCurrentDurability(tool) < ToolHelper.getMaxDurability(tool)) {
			if (PsiUtils.extractPsiExact((EntityPlayer)entity, PSI_COST) >= PSI_COST) {
				ToolHelper.healTool(tool, 1, (EntityPlayer)entity);
			}
		}
	}
	
	@Override
	public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
		if (entity instanceof EntityPlayer
				&& newDamage >= 1
				&& Toggle.getToggleState(tool, identifier)) {
			if (PsiUtils.extractPsiExact((EntityPlayer)entity, PSI_COST) >= PSI_COST) {
				--newDamage;
			}
		}
		return super.onToolDamage(tool, damage, newDamage, entity);
	}
}
