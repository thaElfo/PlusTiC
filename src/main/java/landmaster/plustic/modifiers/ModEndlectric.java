package landmaster.plustic.modifiers;

import java.util.*;
import baubles.api.*;
import baubles.api.cap.*;
import cofh.api.energy.*;
import landmaster.plustic.config.Config;
import landmaster.plustic.toggle.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.utils.*;

public class ModEndlectric extends ModifierTrait {
	public static final int ENERGY_DRAW = 100;
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
					if (iv[i] != null && iv[i].getItem() instanceof IEnergyContainerItem && !Config.isInEndlectricBlacklist(iv[i])) {
						IEnergyContainerItem energy = ((IEnergyContainerItem)iv[i].getItem());
						if (energy.extractEnergy(iv[i], ENERGY_DRAW, true) >= ENERGY_DRAW) {
							ep.inventory.markDirty();
							energy.extractEnergy(iv[i], ENERGY_DRAW, false);
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
				if (is != null && is.getItem() instanceof IEnergyContainerItem && !Config.isInEndlectricBlacklist(is)) {
					IEnergyContainerItem energy = ((IEnergyContainerItem)is.getItem());
					if (energy.extractEnergy(is, ENERGY_DRAW, true) >= ENERGY_DRAW) {
						energy.extractEnergy(is, ENERGY_DRAW, false);
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
		System.out.println("Augment damage by "+data.level);
		return old + data.level*1.7f;
	}
}
