package landmaster.plustic;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;

import gnu.trove.map.hash.*;
import landmaster.plustic.proxy.*;
import landmaster.plustic.config.*;
import landmaster.plustic.modules.*;
import landmaster.plustic.net.*;
import landmaster.plustic.util.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.oredict.*;
import net.minecraftforge.fml.common.Mod.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

@Mod(modid = PlusTiC.MODID, name = PlusTiC.NAME, version = PlusTiC.VERSION, dependencies = PlusTiC.DEPENDS)
public class PlusTiC {
	public static final String MODID = "plustic";
	public static final String NAME = "PlusTiC";
	public static final String VERSION = "4.4.0.0";
	public static final String DEPENDS = "required-after:mantle;required-after:tconstruct;"
			+ "after:Mekanism;after:BiomesOPlenty;after:biomesoplenty;"
			+ "after:Botania;after:botania;after:advancedRocketry;"
			+ "after:armorplus;after:EnderIO;after:enderio;after:projectred-exploration;"
			+ "after:thermalfoundation;after:substratum;after:draconicevolution;"
			+ "after:landcore;after:tesla;after:baubles;after:actuallyadditions;"
			+ "after:natura;after:Psi;after:avaritia;after:landcore;after:landcraft";
	
	public static Config config;
	
	@Mod.Instance(PlusTiC.MODID)
	public static PlusTiC INSTANCE;
	
	@SidedProxy(serverSide = "landmaster.plustic.proxy.CommonProxy", clientSide = "landmaster.plustic.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static final Logger log = LogManager.getLogger(
			MODID.toUpperCase(Locale.US/* to avoid problems with Turkish */));
	
	public static final Map<String, Material> materials = new THashMap<>();
	public static final Map<String, MaterialIntegration> materialIntegrations = new THashMap<>();
	
	public static final BowMaterialStats justWhy = new BowMaterialStats(0.2f, 0.4f, -1f);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		(config = new Config(event)).sync();
		
		proxy.initEntities();
		
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
		
		ModuleTools.init();
		
		integrate(materials, materialIntegrations);
		
		ModuleModifiers.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.initToolGuis();
		proxy.registerKeyBindings();
		PacketHandler.init();
		
		// CURSE YOU, MEKANISM AND ARMORPLUS! YOU REGISTERED THE OREDICTS IN
		// INIT INSTEAD OF PREINIT!
		Utils.setDispItem(materials.get("refinedObsidian"), "mekanism", "Ingot");
		Utils.setDispItem(materials.get("osmium"), "mekanism", "Ingot", 1);
		Utils.setDispItem(materials.get("witherbone"), "armorplus", "wither_bone");
		Utils.setDispItem(materials.get("guardianscale"), "armorplus", "guardian_scale");
		
		// YOU TOO, ACTUALLY ADDITIONS!
		Utils.setDispItem(materials.get("blackquartz"), "actuallyadditions", "itemMisc", 5);
		
		// SAME HERE, AVARITIA!
		Utils.setDispItem(materials.get("infinity"), "ingotInfinity");
		
		Utils.setDispItem(materials.get("sapphire"), "gemSapphire");
		Utils.setDispItem(materials.get("ruby"), "gemRuby");
		Utils.setDispItem(materials.get("peridot"), "gemPeridot");
		
		initRecipes();
	}
	
	private static void initRecipes() {
		Item bronzeNugget = Item.REGISTRY.getObject(new ResourceLocation(MODID, "bronzenugget"));
		Item bronzeIngot = Item.REGISTRY.getObject(new ResourceLocation(MODID, "bronzeingot"));
		
		Block osmiridiumBlock = Block.REGISTRY.getObject(new ResourceLocation(MODID, "osmiridiumblock"));
		Item osmiridiumIngot = Item.REGISTRY.getObject(new ResourceLocation(MODID, "osmiridiumingot"));
		Item osmiridiumNugget = Item.REGISTRY.getObject(new ResourceLocation(MODID, "osmiridiumnugget"));
		
		Block alumiteBlock = Block.REGISTRY.getObject(new ResourceLocation(MODID, "alumiteblock"));
		Item alumiteIngot = Item.REGISTRY.getObject(new ResourceLocation(MODID, "alumiteingot"));
		Item alumiteNugget = Item.REGISTRY.getObject(new ResourceLocation(MODID, "alumitenugget"));
		
		Block mirionBlock = Block.REGISTRY.getObject(new ResourceLocation(MODID, "mirionblock"));
		Item mirionIngot = Item.REGISTRY.getObject(new ResourceLocation(MODID, "mirioningot"));
		Item mirionNugget = Item.REGISTRY.getObject(new ResourceLocation(MODID, "mirionnugget"));
		
		Block invarBlock = Block.REGISTRY.getObject(new ResourceLocation(MODID, "invarblock"));
		Item invarIngot = Item.REGISTRY.getObject(new ResourceLocation(MODID, "invaringot"));
		Item invarNugget = Item.REGISTRY.getObject(new ResourceLocation(MODID, "invarnugget"));
		
		if (bronzeNugget != null) {
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(bronzeIngot), "III", "III", "III", 'I', "nuggetBronze"));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(bronzeNugget, 9), "ingotBronze"));
		}
		if (osmiridiumNugget != null) {
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(osmiridiumBlock), "III", "III", "III", 'I', "ingotOsmiridium"));
			GameRegistry.addShapelessRecipe(new ItemStack(osmiridiumIngot, 9), osmiridiumBlock);
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(osmiridiumIngot), "III", "III", "III", 'I', "nuggetOsmiridium"));
			GameRegistry.addShapelessRecipe(new ItemStack(osmiridiumNugget, 9), osmiridiumIngot);
		}
		if (alumiteNugget != null) {
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(alumiteBlock), "III", "III", "III", 'I', "ingotAlumite"));
			GameRegistry.addShapelessRecipe(new ItemStack(alumiteIngot, 9), alumiteBlock);
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(alumiteIngot), "III", "III", "III", 'I', "nuggetAlumite"));
			GameRegistry.addShapelessRecipe(new ItemStack(alumiteNugget, 9), alumiteIngot);
		}
		if (mirionNugget != null) {
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(mirionBlock), "III", "III", "III", 'I', "ingotMirion"));
			GameRegistry.addShapelessRecipe(new ItemStack(mirionIngot, 9), mirionBlock);
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(mirionIngot), "III", "III", "III", 'I', "nuggetMirion"));
			GameRegistry.addShapelessRecipe(new ItemStack(mirionNugget, 9), mirionIngot);
		}
		if (invarNugget != null) {
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(invarBlock), "III", "III", "III", 'I', "ingotInvar"));
			GameRegistry.addShapelessRecipe(new ItemStack(invarIngot, 9), invarBlock);
			GameRegistry.addRecipe(
					new ShapedOreRecipe(new ItemStack(invarIngot), "III", "III", "III", 'I', "nuggetInvar"));
			GameRegistry.addShapelessRecipe(new ItemStack(invarNugget, 9), invarIngot);
		}
	}

	private static void integrate(Map<String,Material> materials,
			Map<String,MaterialIntegration> materialIntegrations) {
		materials.forEach((k, v) -> {
			MaterialIntegration mi;
			if (v.getFluid() != null && v.getFluid() != TinkerFluids.emerald) {
				mi = new MaterialIntegration(v, v.getFluid(), StringUtils.capitalize(k)).toolforge();
			} else {
				mi = new MaterialIntegration(v);
			}
			mi.integrate();
			mi.integrateRecipes();
			materialIntegrations.put(k, mi);
		});
	}
}
