package landmaster.plustic.modifiers;

import java.util.*;
import baubles.api.*;
import baubles.api.cap.*;
import landmaster.plustic.api.*;
import landmaster.plustic.config.*;
import landmaster.plustic.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.utils.*;

public class ModEndlectric extends ModifierTrait {
	public static final int ENERGY_DRAW = 200;
	public static final ModEndlectric endlectric = new ModEndlectric();
	
	public ModEndlectric() {
		super("endlectric", 0x5AFCDC, 5, 0);
		Toggle.toggleable.add(identifier);
	}
	
	@Override
	public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
		energydraw:
		if (player instanceof EntityPlayer && Toggle.getToggleState(tool, identifier)) {
			EntityPlayer ep = (EntityPlayer)player;
			List<ItemStack[]> ivs = Arrays.asList(ep.inventory.mainInventory,ep.inventory.armorInventory,ep.inventory.offHandInventory);
			for (ItemStack[] iv: ivs) {
				for (int i=0; i<iv.length; ++i) {
					if (!Config.isInEndlectricBlacklist(iv[i])) {
						if (Utils.extractEnergy(iv[i], ENERGY_DRAW, true) >= ENERGY_DRAW) {
							ep.inventory.markDirty();
							Utils.extractEnergy(iv[i], ENERGY_DRAW, false);
							newDamage = augmentDamage(newDamage, TinkerUtil.getModifierTag(tool, identifier));
							break energydraw;
						}
					}
				}
			}
			IBaublesItemHandler ib = BaublesApi.getBaublesHandler(ep);
			for (int i=0; i<ib.getSlots(); ++i) {
				ItemStack is = ib.getStackInSlot(i);
				is = ItemStack.copyItemStack(is);
				if (!Config.isInEndlectricBlacklist(is)) {
					if (Utils.extractEnergy(is, ENERGY_DRAW, true) >= ENERGY_DRAW) {
						Utils.extractEnergy(is, ENERGY_DRAW, false);
						newDamage = augmentDamage(newDamage, TinkerUtil.getModifierTag(tool, identifier));
						ib.setStackInSlot(i, is);
						break energydraw;
					}
				}
			}
		}
		return super.damage(tool, player, target, damage, newDamage, isCritical);
	}
	private float augmentDamage(float old, NBTTagCompound modifierTag) {
		ModifierNBT data = ModifierNBT.readTag(modifierTag);
		return old + 5*data.level;
	}
}
