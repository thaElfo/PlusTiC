package landmaster.plustic.traits;

import baubles.api.*;
import baubles.api.cap.*;
import landmaster.plustic.api.*;

import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;
import vazkii.botania.api.mana.*;

public class Mana extends AbstractTrait {
	public static final int MANA_DRAW = 100;
	public static final Mana mana = new Mana();
	
	public Mana() {
		super("mana", 0x54E5FF);
		Toggle.toggleable.add(identifier);
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!world.isRemote
				&& entity instanceof EntityPlayer
				&& ToolHelper.getCurrentDurability(tool) < ToolHelper.getMaxDurability(tool)
				&& Toggle.getToggleState(tool, identifier)) {
			EntityPlayer ep = (EntityPlayer)entity;
			List<ItemStack[]> ivs = Arrays.asList(ep.inventory.mainInventory,ep.inventory.armorInventory,ep.inventory.offHandInventory);
			for (ItemStack[] iv: ivs) {
				for (int i=0; i<iv.length; ++i) {
					if (ManaItemHandler.requestManaExactForTool(iv[i], ep, MANA_DRAW, true)) {
						ep.inventory.markDirty();
						ToolHelper.healTool(tool, 1, ep);
						return;
					}
				}
			}
			IBaublesItemHandler ib = BaublesApi.getBaublesHandler(ep);
			for (int i=0; i<ib.getSlots(); ++i) {
				ItemStack is = ib.getStackInSlot(i);
				is = ItemStack.copyItemStack(is);
				if (ManaItemHandler.requestManaExactForTool(is, ep, MANA_DRAW, true)) {
					ToolHelper.healTool(tool, 1, ep);
					ib.setStackInSlot(i, is);
					return;
				}
			}
		}
	}
	
	@Override
	public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
		manadraw:
		if (entity instanceof EntityPlayer && newDamage > 0 && Toggle.getToggleState(tool, identifier)) {
			EntityPlayer ep = (EntityPlayer)entity;
			List<ItemStack[]> ivs = Arrays.asList(ep.inventory.mainInventory,ep.inventory.armorInventory,ep.inventory.offHandInventory);
			for (ItemStack[] iv: ivs) {
				for (int i=0; i<iv.length; ++i) {
					if (ManaItemHandler.requestManaExactForTool(iv[i], ep, MANA_DRAW, true)) {
						ep.inventory.markDirty();
						if (--newDamage <= 0) break manadraw;
					}
				}
			}
			IBaublesItemHandler ib = BaublesApi.getBaublesHandler(ep);
			for (int i=0; i<ib.getSlots(); ++i) {
				ItemStack is = ib.getStackInSlot(i);
				is = ItemStack.copyItemStack(is);
				if (ManaItemHandler.requestManaExactForTool(is, ep, MANA_DRAW, true)) {
					ib.setStackInSlot(i, is);
					if (--newDamage <= 0) break manadraw;
				}
			}
		}
		return super.onToolDamage(tool, damage, newDamage, entity);
	}
}
