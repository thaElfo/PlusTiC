package landmaster.plustic.tools;

import java.util.*;

import javax.annotation.*;

import landmaster.plustic.modules.*;
import landmaster.plustic.tools.nbt.*;
import landmaster.plustic.tools.stats.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.energy.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.*;
import slimeknights.tconstruct.library.tools.*;
import slimeknights.tconstruct.library.utils.*;
import slimeknights.tconstruct.tools.*;

public class ToolLaserGun extends TinkerToolCore implements cofh.api.energy.IEnergyContainerItem {
	public static final float DURABILITY_MODIFIER = 1.5f;
	
	public static final String ATTACK_DURATION_TAG = "AttackDuration";
	
	private int maxAttackDuration(ItemStack is) {
		return (int)(20 / this.attackSpeed());
	}
	
	private int energyPerAttack(ItemStack is) {
		return 100;
	}
	
	private int getFullEnergy(ItemStack is) {
		return (new ToolEnergyNBT(TagUtil.getToolTag(is))).energy;
	}
	
	public ToolLaserGun() {
		super(PartMaterialType.handle(TinkerTools.toughToolRod),
				PartMaterialType.head(ModuleTools.pipe_piece),
				new PartMaterialType(ModuleTools.laser_medium, LaserMediumMaterialStats.TYPE),
				new PartMaterialType(ModuleTools.battery_cell, BatteryCellMaterialStats.TYPE));
		
		this.addCategory(Category.WEAPON);
		
		this.setUnlocalizedName("laser_gun").setRegistryName("laser_gun");
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		NBTTagCompound nbt = TagUtil.getTagSafe(stack);
		nbt.setInteger(ATTACK_DURATION_TAG, MathHelper.clamp_int(nbt.getInteger(ATTACK_DURATION_TAG)-1, 0, Integer.MAX_VALUE));
		stack.setTagCompound(nbt);
	}
	
	@Override
	public void getSubItems(@Nonnull Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		this.addDefaultSubItems(subItems, null, null, TinkerMaterials.prismarine, TinkerMaterials.electrum);
	}

	@Override
	protected LaserNBT buildTagData(List<Material> materials) {
		LaserNBT nbt = new LaserNBT();
		nbt.head(materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD));
		nbt.handle(materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE));
		nbt.laserMedium(materials.get(2).getStatsOrUnknown(LaserMediumMaterialStats.TYPE));
		nbt.batteryCell(materials.get(3).getStatsOrUnknown(BatteryCellMaterialStats.TYPE));
		return nbt;
	}

	@Override
	public float damagePotential() {
		return 1.0f;
	}

	@Override
	public double attackSpeed() {
		return 3;
	}
	
	@Override
	public List<String> getInformation(ItemStack stack, boolean detailed) {
		TooltipBuilder info = new TooltipBuilder(stack);
		
	    info.addDurability(!detailed);
	    // for energy stored
	    info.add(String.format(TextFormatting.AQUA+"%s RF", this.getEnergyStored(stack)));
	    
	    info.addAttack();
	    
	    if (ToolHelper.getFreeModifiers(stack) > 0) {
	    	info.addFreeModifiers();
	    }
	    
		if (detailed) {
			info.addModifierInfo();
		}
		
	    return info.getTooltip();
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return false; // can only use laser attack
	}
	
	/**
	 * <strong>This is the real laser attack.</strong>
	 * {@inheritDoc}
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		return Optional.ofNullable(EntityUtil.raytraceEntityPlayerLook(playerIn, 20))
		.map(rtr -> rtr.entityHit)
		.map(ent -> {
			int energyTaken = this.energyPerAttack(itemStackIn);
			NBTTagCompound nbt = TagUtil.getTagSafe(itemStackIn);
			if (this.extractEnergy(itemStackIn, energyTaken, true) >= energyTaken
					&& nbt.getInteger(ATTACK_DURATION_TAG) <= 0) { // able to attack?
				if (ToolHelper.attackEntity(itemStackIn, this, playerIn, ent)) { // try to attack
					this.extractEnergy(itemStackIn, energyTaken, false); // if successful, drain energy
					nbt.setInteger(ATTACK_DURATION_TAG, this.maxAttackDuration(itemStackIn));
					itemStackIn.setTagCompound(nbt);
					return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
				}
			}
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		}).orElse(new ActionResult<>(EnumActionResult.PASS, itemStackIn));
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		if (!container.hasTagCompound()) {
			container.setTagCompound(new NBTTagCompound());
		}
		int energy = container.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(this.getFullEnergy(container) - energy, Math.min(this.getFullEnergy(container), maxReceive));
		
		if (!simulate) {
			energy += energyReceived;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy")) {
			return 0;
		}
		int energy = container.getTagCompound().getInteger("Energy");
		int energyExtracted = Math.min(energy, Math.min(this.getFullEnergy(container), maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy")) {
			return 0;
		}
		return container.getTagCompound().getInteger("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		return this.getFullEnergy(container);
	}
	
	private class Energy implements IEnergyStorage {
		ItemStack is;
		public Energy(ItemStack is) {
			this.is = is;
		}
		
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return ToolLaserGun.this.receiveEnergy(is, maxReceive, simulate);
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return ToolLaserGun.this.extractEnergy(is, maxExtract, simulate);
		}

		@Override
		public int getEnergyStored() {
			return ToolLaserGun.this.getEnergyStored(is);
		}

		@Override
		public int getMaxEnergyStored() {
			return ToolLaserGun.this.getMaxEnergyStored(is);
		}

		@Override
		public boolean canExtract() {
			return true;
		}

		@Override
		public boolean canReceive() {
			return true;
		}
	}
	
	private class Provider implements ICapabilityProvider {
		Energy energy;
		
		public Provider(ItemStack is) {
			energy = new Energy(is);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CapabilityEnergy.ENERGY;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == CapabilityEnergy.ENERGY) {
				return (T)energy;
			}
			return null;
		}
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack is, NBTTagCompound capNbt) {
		return new Provider(is);
	}
}
