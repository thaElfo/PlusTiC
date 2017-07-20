package landmaster.plustic;

import java.util.*;
import java.util.Optional;
import java.util.function.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;

import de.ellpeck.actuallyadditions.mod.items.InitItems;
import gnu.trove.map.hash.*;
import landmaster.plustic.proxy.*;
import landmaster.plustic.api.*;
import landmaster.plustic.config.*;
import landmaster.plustic.modules.*;
import landmaster.plustic.net.*;
import landmaster.plustic.util.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.Mod.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;
import slimeknights.tconstruct.tools.*;

@Mod.EventBusSubscriber
@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDS, useMetadata = true, acceptedMinecraftVersions = "[1.12, 1.13)")
public class PlusTiC {
	public static Config config;
	
	@Instance(ModInfo.MODID)
	public static PlusTiC INSTANCE;
	
	@SidedProxy(serverSide = "landmaster.plustic.proxy.CommonProxy", clientSide = "landmaster.plustic.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static final Logger log = LogManager.getLogger(
			ModInfo.MODID.toUpperCase(Locale.US/* to avoid problems with Turkish */));
	
	public static final Map<String, Material> materials = new THashMap<>();
	public static final Map<String, MaterialIntegration> materialIntegrations = new THashMap<>();
	
	public static final Map<String, Predicate<String>> materialIntegrationConditions = new THashMap<>();
	
	public static final BowMaterialStats justWhy = new BowMaterialStats(0.2f, 0.4f, -1f);
	
	private static final String[] renamesToHandle = new String[] { "osmium", "titanium", "iridium" };
	
	@SubscribeEvent
	public static void missingBlockMappings(RegistryEvent.MissingMappings<Block> event) {
		event.getMappings().forEach(mapping -> {
			for (String name: renamesToHandle) {
				if (mapping.key.equals(new ResourceLocation(ModInfo.MODID, ModInfo.MODID+".molten_"+name))) {
					Optional.ofNullable(FluidRegistry.getFluid(name))
					.map(Fluid::getBlock)
					.ifPresent(mapping::remap);
				}
			}
		});
	}
	
	@SubscribeEvent
	public static void missingItemMappings(RegistryEvent.MissingMappings<Item> event) {
		event.getMappings().forEach(mapping -> {
			for (String name: renamesToHandle) {
				if (mapping.key.equals(new ResourceLocation(ModInfo.MODID, ModInfo.MODID+".molten_"+name))) {
					Optional.ofNullable(FluidRegistry.getFluid(name))
					.map(Fluid::getBlock)
					.map(Item::getItemFromBlock)
					.ifPresent(mapping::remap);
				}
			}
		});
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		(config = new Config(event)).sync();
		
		proxy.initEntities();
		
		// TODO add more modules when needed
		
		ModuleBase.init();
		
		ModuleBoP.init();
		ModuleMekanism.init();
		ModuleBotania.init();
		ModuleAdvRocketry.init();
		ModuleArmorPlus.init();
		ModuleEnderIO.init();
		ModuleTF.init();
		ModuleDraconicEvolution.init();
		ModuleActAdd.init();
		ModuleNatura.init();
		ModulePsi.init();
		ModuleAvaritia.init();
		ModuleLandCraft.init();
		ModuleLandCore.init();
		ModuleMFR.init();
		ModuleGalacticraft.init();
		ModuleSurvivalist.init();
		ModuleProjectE.init();
		ModuleGemsPlus.init();
		
		ModuleTools.init();
		
		ModuleModifiers.init();
		
		preIntegrate(materials, materialIntegrations);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.initToolGuis();
		proxy.registerKeyBindings();
		PacketHandler.init();
		
		ModuleBase.init2();
		ModuleNatura.init2();
		
		final Material Void = materials.get("Void");
		if (Void != null) {
			ItemStack voidStack = new ItemStack(InitItems.itemCrystal, 1, 3);
			Void.addItem(voidStack, 1, Material.VALUE_Ingot);
			Void.setRepresentativeItem(voidStack);
		}
		
		Utils.setDispItem(materials.get("phoenixite"), "gemPhoenixite");
		
		// This is here for historical reasons, as Mekanism has fixed their thing.
		Utils.setDispItem(materials.get("refinedObsidian"), "ingotRefinedObsidian");
		Utils.setDispItem(materials.get("osmium"), "ingotOsmium");
		Utils.setDispItem(materials.get("refinedGlowstone"), "ingotRefinedGlowstone");
		
		// NOT REGISTERING YOUR OREDICTS IN PREINIT, ARMORPLUS?
		Utils.setDispItem(materials.get("witherbone"), "armorplus", "wither_bone");
		Utils.setDispItem(materials.get("guardianscale"), "armorplus", "guardian_scale");
		
		// YOU TOO, ACTUALLY ADDITIONS?
		Utils.setDispItem(materials.get("blackquartz"), "gemQuartzBlack");
		
		// SAME HERE, AVARITIA?
		Utils.setDispItem(materials.get("infinity"), "ingotInfinity");
		
		Utils.setDispItem(materials.get("sapphire"), "gemSapphire");
		Utils.setDispItem(materials.get("ruby"), "gemRuby");
		Utils.setDispItem(materials.get("peridot"), "gemPeridot");
		
		{
			// TODO add more materials to force out when needed
			if (Config.botania && Config.forceOutNaturalPledgeMaterials) {
				Utils.forceOutModsMaterial("terrasteel", "botanicaladdons");
				Utils.forceOutModsMaterial("elementium", "botanicaladdons");
				Utils.forceOutModsMaterial("manasteel", "botanicaladdons");
				Utils.forceOutModsMaterial("livingwood", "botanicaladdons");
			}
		}
		
		integrate(materials, materialIntegrations, materialIntegrationConditions);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Seriously? Registering oredicts *this* late? -_-
		Utils.setDispItem(materials.get("desh"), "ingotDesh");
	}
	
	private static void preIntegrate(Map<String,Material> materials,
			Map<String,MaterialIntegration> materialIntegrations) {
		materials.forEach((k, v) -> {
			if (!materialIntegrations.containsKey(k)) {
				MaterialIntegration mi;
				if (v.getFluid() != null && v.getFluid() != TinkerFluids.emerald) {
					mi = new MaterialIntegration(v, v.getFluid(), StringUtils.capitalize(k)).toolforge();
				} else {
					mi = new MaterialIntegration(v);
				}
				mi.preInit();
				materialIntegrations.put(k, mi);
			}
		});
	}
	
	private static void integrate(Map<String,Material> materials,
			Map<String,MaterialIntegration> materialIntegrations,
			Map<String, Predicate<String>> materialIntegrationConditions) {
		materialIntegrations.forEach((k, mi) -> {
			if (materialIntegrationConditions.getOrDefault(k, e -> true).test(k)) mi.integrateRecipes();
		});
		
		Utils.displace(TinkerMaterials.wood.getIdentifier()); // so that natura woods are prioritized
	}
}
