package landmaster.plustic.util;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.*;

import cofh.api.energy.*;
import landmaster.plustic.*;
import landmaster.plustic.api.*;
import landmaster.plustic.block.*;
import landmaster.plustic.fluids.*;
import mcjty.lib.tools.*;
import net.darkhax.tesla.capability.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.energy.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.oredict.*;
import slimeknights.tconstruct.smeltery.block.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class Utils {
	private static final Map<String, Material> tinkerMaterials;
	
	static {
		try {
			Field temp = TinkerRegistry.class.getDeclaredField("materials");
			temp.setAccessible(true);
			tinkerMaterials = (Map<String, Material>)MethodHandles.lookup().unreflectGetter(temp).invokeExact();
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
	
	/**
	 * Pushes a material into a lower priority.
	 * @param displace the identifier of the material to be pushed
	 */
	public static void displace(String displace) {
		Material displaced = tinkerMaterials.remove(displace);
		tinkerMaterials.put(displace, displaced);
	}
	
	public static boolean matchesOre(ItemStack is, String od) {
		return OreDictionary.doesOreNameExist(od) && !ItemStackTools.isEmpty(is) && ArrayUtils.contains(OreDictionary.getOreIDs(is), OreDictionary.getOreID(od));
	}
	
	public static AxisAlignedBB AABBfromVecs(Vec3d v1, Vec3d v2) {
		return new AxisAlignedBB(v1.xCoord, v1.yCoord, v1.zCoord, v2.xCoord, v2.yCoord, v2.zCoord);
	}
	
	public static void addModifierItem(Modifier modifier, String modid, String name) {
		addModifierItem(modifier, modid, name, 0);
	}
	
	public static void addModifierItem(Modifier modifier, String modid, String name, int meta) {
		addModifierItem(modifier, modid, name, meta, 1, 1);
	}
	
	public static void addModifierItem(Modifier modifier, String modid, String name, int meta, int needed, int matched) {
		if (modifier == null) return;
		ItemStack is = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(modid,name)), 1, meta);
		modifier.addItem(is, needed, matched);
	}
	
	public static FluidMolten fluidMetal(String name, int color) {
		return registerFluid(new FluidMolten(name,color));
	}
	
	public static void initFluidMetal(Fluid fluid) {
		registerMoltenBlock(fluid);
        PlusTiC.proxy.registerFluidModels(fluid);
	}
	
	public static <T extends Fluid> T registerFluid(T fluid) {
		fluid.setUnlocalizedName(ModInfo.MODID+"."+fluid.getName().toLowerCase(Locale.US));
		FluidRegistry.registerFluid(fluid);
		return fluid;
	}
	
	public static <T extends Block> T registerBlock(T block, String name) {
		block.setUnlocalizedName(ModInfo.MODID+"."+name);
		block.setRegistryName(ModInfo.MODID+"."+name);
		Item ib = new ItemBlock(block).setRegistryName(block.getRegistryName());
		GameRegistry.register(block);
		GameRegistry.register(ib);
		return block;
	}
	
	public static BlockMolten registerMoltenBlock(Fluid fluid) {
		BlockMolten block = new BlockMolten(fluid);
		return registerBlock(block, "molten_" + fluid.getName());
	}
	
	public static void setDispItem(Material mat, String modid, String name) {
		if (mat == null) return;
		mat.setRepresentativeItem(Item.REGISTRY.getObject(new ResourceLocation(modid, name)));
	}
	public static void setDispItem(Material mat, String modid, String name, int meta) {
		if (mat == null) return;
		ItemStack is = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(modid, name)), 1, meta);
		mat.setRepresentativeItem(is);
	}
	public static void setDispItem(Material mat, String ore) {
		List<ItemStack> ores = ItemStackTools.getOres(ore);
		if (mat == null || ores.size() < 1) return;
		mat.setRepresentativeItem(ores.get(0));
	}
	
	public static int gcd(int a, int b, int... rest) {
		if (rest.length > 0) {
			int[] rest1 = new int[rest.length-1];
			System.arraycopy(rest, 1, rest1, 0, rest1.length);
			return gcd(gcd(a,b), rest[0], rest1);
		}
		return b==0 ? a : gcd(b, a%b);
	}

	public static void teleportPlayerTo(EntityPlayerMP player, Coord4D coord) {
		if (player.dimension != coord.dimensionId) {
			int id = player.dimension;
			WorldServer oldWorld = player.mcServer.worldServerForDimension(player.dimension);
			player.dimension = coord.dimensionId;
			WorldServer newWorld = player.mcServer.worldServerForDimension(player.dimension);
			player.connection.sendPacket(new SPacketRespawn(player.dimension, player.getEntityWorld().getDifficulty(), newWorld.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
			oldWorld.removeEntityDangerously(player);
			player.isDead = false;
	
			if(player.isEntityAlive())
			{
				newWorld.spawnEntity(player);
				player.setLocationAndAngles(coord.xCoord+0.5, coord.yCoord+1, coord.zCoord+0.5, player.rotationYaw, player.rotationPitch);
				newWorld.updateEntityWithOptionalForce(player, false);
				player.setWorld(newWorld);
			}
	
			player.mcServer.getPlayerList().preparePlayer(player, oldWorld);
			player.connection.setPlayerLocation(coord.xCoord+0.5, coord.yCoord+1, coord.zCoord+0.5, player.rotationYaw, player.rotationPitch);
			player.interactionManager.setWorld(newWorld);
			player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, newWorld);
			player.mcServer.getPlayerList().syncPlayerInventory(player);
	
			for(PotionEffect potioneffect : player.getActivePotionEffects())
			{
				player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
			}
	
			player.connection.sendPacket(new SPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel)); // Force XP sync
	
			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, id, coord.dimensionId);
		}
		else {
			player.connection.setPlayerLocation(coord.xCoord+0.5, coord.yCoord+1, coord.zCoord+0.5, player.rotationYaw, player.rotationPitch);
		}
	}
	
	private static final MethodHandle getCollisionBoundingBoxM;
	
	static {
		try {
			MethodHandle temp;
			try {
				temp = MethodHandles.lookup().findVirtual(IBlockState.class, "func_185890_d", MethodType.methodType(AxisAlignedBB.class, IBlockAccess.class, BlockPos.class));
			} catch (NoSuchMethodException e) {
				temp = MethodHandles.lookup().findVirtual(IBlockState.class, "func_185890_d", MethodType.methodType(AxisAlignedBB.class, World.class, BlockPos.class));
			}
			getCollisionBoundingBoxM = temp;
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
	
	public static AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
		try {
			return (AxisAlignedBB)getCollisionBoundingBoxM.invoke(state, world, pos);
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}

	public static boolean canTeleportTo(EntityPlayer player, Coord4D dest) {
		if (dest == null) return false;
		for (int i=1; i<=2; ++i) {
			if (getCollisionBoundingBox(dest.add(0, i, 0).blockState(), dest.world(), dest.pos()) != null) {
				return false;
			}
		}
		return true;
	}
	
	public static int extractEnergy(ItemStack is, int amount, boolean simulate) {
		if (is != null) {
			if (is.getItem() instanceof IEnergyContainerItem) {
				IEnergyContainerItem energy = (IEnergyContainerItem)is.getItem();
				return energy.extractEnergy(is, amount, simulate);
			}
			if (is.hasCapability(CapabilityEnergy.ENERGY, null)) {
				return is.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(amount, simulate);
			}
			if (Loader.isModLoaded("tesla") && is.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null)) {
				return (int)is.getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null).takePower(amount, simulate);
			}
		}
		return 0;
	}
	
	public static class ItemMatGroup {
		public Item nugget, ingot;
		public Block block;
		public ItemMatGroup() {}
		public ItemMatGroup(Item nugget, Item ingot, Block block) {
			this.nugget = nugget;
			this.ingot = ingot;
			this.block = block;
		}
	}
	
	public static ItemMatGroup registerMatGroup(String name) {
		ItemMatGroup img = new ItemMatGroup();
		img.nugget = new Item().setUnlocalizedName(name+"nugget")
				.setRegistryName(name+"nugget");
		img.nugget.setCreativeTab(TinkerRegistry.tabGeneral);
		GameRegistry.register(img.nugget);
		OreDictionary.registerOre("nugget"+StringUtils.capitalize(name), img.nugget);
		PlusTiC.proxy.registerItemRenderer(img.nugget, 0, name+"nugget");
		
		img.ingot = new Item().setUnlocalizedName(name+"ingot")
				.setRegistryName(name+"ingot");
		img.ingot.setCreativeTab(TinkerRegistry.tabGeneral);
		GameRegistry.register(img.ingot);
		OreDictionary.registerOre("ingot"+StringUtils.capitalize(name), img.ingot);
		PlusTiC.proxy.registerItemRenderer(img.ingot, 0, name+"ingot");
		
		img.block = new MetalBlock(name+"block");
		img.block.setCreativeTab(TinkerRegistry.tabGeneral);
		ItemBlock bitem = new ItemBlock(img.block);
		GameRegistry.register(img.block);
		GameRegistry.register(bitem, img.block.getRegistryName());
		OreDictionary.registerOre("block"+StringUtils.capitalize(name), img.block);
		PlusTiC.proxy.registerItemRenderer(bitem, 0, name+"block");
		
		return img;
	}
}
