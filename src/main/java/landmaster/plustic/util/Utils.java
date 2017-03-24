package landmaster.plustic.util;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.modifiers.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.oredict.*;
import slimeknights.tconstruct.smeltery.block.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.modifiers.*;

public class Utils {
	public static void integrate(Map<String,Material> materials,Map<String,MaterialIntegration> materialIntegrations) {
		materials.entrySet().forEach(ent -> {
			MaterialIntegration mi;
			if (ent.getValue().getFluid() != null)
				mi = new MaterialIntegration(ent.getValue(),ent.getValue().getFluid(),StringUtils.capitalize(ent.getKey())).toolforge();
			else
				mi = new MaterialIntegration(ent.getValue());
			mi.integrate();
			mi.integrateRecipes();
			materialIntegrations.put(ent.getKey(), mi);
		});
	}
	
	public static void registerModifiers() {
		if (Config.enderIO && Loader.isModLoaded("EnderIO")) {
			TinkerRegistry.registerModifier(ModEndlectric.endlectric);
			addModifierItem(ModEndlectric.endlectric, "enderio", "itemBasicCapacitor", 2);
		}
	}
	
	public static void addModifierItem(Modifier modifier, String modid, String name) {
		addModifierItem(modifier, modid, name, 0);
	}
	
	public static void addModifierItem(Modifier modifier, String modid, String name, int meta) {
		if (modifier == null) return;
		ItemStack is = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(modid,name)), 1, meta);
		modifier.addItem(is, 1, 1);
	}
	
	public static FluidMolten fluidMetal(String name, int color) {
		return registerFluid(new FluidMolten(name,color));
	}
	
	public static void initFluidMetal(Fluid fluid) {
		Utils.registerMoltenBlock(fluid);
        PlusTiC.proxy.registerFluidModels(fluid);
	}
	
	public static <T extends Fluid> T registerFluid(T fluid) {
		fluid.setUnlocalizedName(PlusTiC.MODID+"."+fluid.getName().toLowerCase(Locale.US));
		FluidRegistry.registerFluid(fluid);
		return fluid;
	}
	
	public static <T extends Block> T registerBlock(T block, String name) {
		block.setUnlocalizedName(PlusTiC.MODID+"."+name);
		block.setRegistryName(PlusTiC.MODID+"."+name);
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
		List<ItemStack> ores = OreDictionary.getOres(ore);
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
			player.connection.sendPacket(new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), newWorld.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
			oldWorld.removeEntityDangerously(player);
			player.isDead = false;
	
			if(player.isEntityAlive())
			{
				newWorld.spawnEntityInWorld(player);
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

	public static boolean canTeleportTo(EntityPlayer player, Coord4D dest) {
		if (dest == null) return false;
		for (int i=1; i<=2; ++i) {
			if (dest.add(0, i, 0).blockState().getCollisionBoundingBox(dest.world(), dest.pos()) != null) {
				return false;
			}
		}
		return true;
	}
}
