package landmaster.plustic.tools;

import java.util.*;

import javax.annotation.*;

import org.lwjgl.opengl.*;

import landmaster.plustic.*;
import landmaster.plustic.modules.*;
import landmaster.plustic.tools.nbt.*;
import landmaster.plustic.tools.stats.*;
import landmaster.plustic.util.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.energy.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.relauncher.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.*;
import slimeknights.tconstruct.library.tools.*;
import slimeknights.tconstruct.library.utils.*;
import slimeknights.tconstruct.tools.*;

public class ToolLaserGun extends TinkerToolCore implements cofh.api.energy.IEnergyContainerItem {
	public static final float DURABILITY_MODIFIER = 1.5f;
	
	private static float range(ItemStack is) {
		return (new LaserNBT(TagUtil.getToolTag(is))).range;
	}
	
	public static final String ATTACK_DURATION_TAG = "AttackDuration";
	
	private int maxAttackDuration(ItemStack is) {
		return (int)(20 / ToolHelper.getActualAttackSpeed(is));
	}
	
	private int energyPerAttack(ItemStack is) {
		return 100;
	}
	
	private static int getFullEnergy(ItemStack is) {
		return (new ToolEnergyNBT(TagUtil.getToolTag(is))).energy;
	}
	
	private static Optional<ItemStack> getActiveLaserGun(EntityLivingBase entity) {
		return Arrays.stream(EnumHand.values())
		.map(entity::getHeldItem)
		.filter(stack -> stack != null
				&& stack.getItem() instanceof ToolLaserGun
				&& TagUtil.getTagSafe(stack).getInteger(ATTACK_DURATION_TAG) > 0)
		.findFirst();
	}
	
	public static final ResourceLocation LASER_LOC = new ResourceLocation(PlusTiC.MODID, "textures/effects/laserbeam.png");
	
	public ToolLaserGun() {
		super(PartMaterialType.handle(TinkerTools.toughToolRod),
				PartMaterialType.head(ModuleTools.pipe_piece),
				new PartMaterialType(ModuleTools.laser_medium, LaserMediumMaterialStats.TYPE),
				new PartMaterialType(ModuleTools.battery_cell, BatteryCellMaterialStats.TYPE));
		
		this.addCategory(Category.WEAPON);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		this.setUnlocalizedName("laser_gun").setRegistryName("laser_gun");
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderBeam(RenderWorldLastEvent event) { // for this player
		Optional.ofNullable(Minecraft.getMinecraft().player)
		.ifPresent(this::doRenderBeam);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderBeam(RenderPlayerEvent event) { // for other players
		if (!event.getEntityPlayer().equals(Minecraft.getMinecraft().player)) { // exclude this player
			this.doRenderBeam(event.getEntityPlayer());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderBeam(RenderLivingEvent<?> event) { // for other entities
		if (!(event.getEntity() instanceof EntityPlayer)) { // exclude players
			this.doRenderBeam(event.getEntity());
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void doRenderBeam(EntityLivingBase shooter) {
		getActiveLaserGun(shooter)
		.ifPresent(stack -> {
			GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
			
			GlStateManager.pushMatrix();
			
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			
			float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
			double doubleX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double doubleY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double doubleZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            
            Vec3d vec = new Vec3d(doubleX, doubleY+player.getEyeHeight(), doubleZ);
            Vec3d vec0 = shooter.getPositionVector().addVector(0, shooter.getEyeHeight()+0.2, 0);
            Vec3d vec1 = Optional.ofNullable(EntityUtil.raytraceEntityPlayerLook(player, range(stack)))
            		.map(rtr -> rtr.hitVec)
            		.orElse(vec.add(player.getLookVec().scale(range(stack))));
            
            GlStateManager.translate(-doubleX, -doubleY, -doubleZ);
			
			Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            
            Minecraft.getMinecraft().renderEngine.bindTexture(LASER_LOC);
            
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            
            ClientUtils.drawBeam(vec0, vec1, vec, 0.13f);
            
            tessellator.draw();
			
			GlStateManager.popMatrix();
			
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		});
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!worldIn.isRemote) {
			NBTTagCompound nbt = TagUtil.getTagSafe(stack);
			nbt.setInteger(ATTACK_DURATION_TAG, MathHelper.clamp(nbt.getInteger(ATTACK_DURATION_TAG)-1, 0, Integer.MAX_VALUE));
			stack.setTagCompound(nbt);
		}
	}
	
	@Override
	public void getSubItems(@Nonnull Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		this.func_150895_a(itemIn, tab, list);
	}
	
	// for 1.10.2
	public void func_150895_a(@Nonnull Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		this.addDefaultSubItems(subItems, null, null, TinkerMaterials.prismarine, TinkerMaterials.manyullyn);
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
	    info.add(String.format(TextFormatting.AQUA+"%s RF / %s RF", this.getEnergyStored(stack), this.getMaxEnergyStored(stack)));
	    
	    info.addAttack();
	    
	    if (ToolHelper.getFreeModifiers(stack) > 0) {
	    	info.addFreeModifiers();
	    }
	    
		if (detailed) {
			info.addModifierInfo();
		}
		
	    return info.getTooltip();
	}
	
	/**
	 * <strong>This is the real laser attack.</strong>
	 * {@inheritDoc}
	 */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		return this.func_77659_a(playerIn.getHeldItem(hand), worldIn, playerIn, hand);
	}
	
	// for 1.10.2
	public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		NBTTagCompound nbt = TagUtil.getTagSafe(itemStackIn);
		
		ActionResult<ItemStack> res = Optional.ofNullable(EntityUtil.raytraceEntityPlayerLook(playerIn, range(itemStackIn)))
		.map(rtr -> rtr.entityHit)
		.map(ent -> {
			int energyTaken = this.energyPerAttack(itemStackIn);
			if (this.extractEnergy(itemStackIn, energyTaken, true) >= energyTaken
					&& nbt.getInteger(ATTACK_DURATION_TAG) <= 0) { // able to attack?
				if (ToolHelper.attackEntity(itemStackIn, this, playerIn, ent)) { // try attacking
					this.extractEnergy(itemStackIn, energyTaken, false); // if success, use energy
					nbt.setInteger(ATTACK_DURATION_TAG, this.maxAttackDuration(itemStackIn));
					itemStackIn.setTagCompound(nbt);
				}
				return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
			}
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		}).orElse(new ActionResult<>(EnumActionResult.PASS, itemStackIn));
		
		return res;
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		if (!container.hasTagCompound()) {
			container.setTagCompound(new NBTTagCompound());
		}
		int energy = container.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(getFullEnergy(container) - energy, Math.min(getFullEnergy(container), maxReceive));
		
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
		int energyExtracted = Math.min(energy, Math.min(getFullEnergy(container), maxExtract));

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
		return getFullEnergy(container);
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
