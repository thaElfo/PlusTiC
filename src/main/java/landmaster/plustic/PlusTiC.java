package landmaster.plustic;

import java.util.*;
import landmaster.plustic.proxy.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.util.*;
import landmaster.plustic.traits.*;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.*;
import net.minecraftforge.fml.common.Mod.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

import slimeknights.tconstruct.shared.TinkerFluids;
import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

/**
 * 
 * @author Landmaster
 */
@Mod(modid = PlusTiC.MODID, name = "PlusTiC", version = PlusTiC.VERSION, dependencies = "required-after:mantle;required-after:tconstruct;after:Mekanism;after:BiomesOPlenty")
public class PlusTiC {
	public static final String MODID = "plustic";
	public static final String VERSION = "1.0";
	
	public static Config config;
	
	@Mod.Instance(PlusTiC.MODID)
	public static PlusTiC instance;
	
	@SidedProxy(serverSide = "landmaster.plustic.proxy.CommonProxy", clientSide = "landmaster.plustic.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static Map<String,Material> materials = new HashMap<>();
	public static Map<String,MaterialIntegration> materialIntegrations = new HashMap<>();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config config = new Config(event);
		config.sync();
		
		BowMaterialStats justWhy = new BowMaterialStats(0.2f, 0.4f, -1f);
		
		if (config.bop && Loader.isModLoaded("BiomesOPlenty")) {
	        Material sapphire = new Material("sapphire",TextFormatting.BLUE);
	        sapphire.addTrait(aquadynamic);
	        sapphire.addItem("gemSapphire", 1, Material.VALUE_Ingot);
	        sapphire.setCraftable(true);
	        proxy.setRenderInfo(sapphire,0x0000FF);
	        TinkerRegistry.addMaterialStats(sapphire, new HeadMaterialStats(700, 5, 6.4f, COBALT));
	        TinkerRegistry.addMaterialStats(sapphire, new HandleMaterialStats(1, 100));
	        TinkerRegistry.addMaterialStats(sapphire, new ExtraMaterialStats(120));
	        TinkerRegistry.addMaterialStats(sapphire, new BowMaterialStats(1,1.5f,4));
	        materials.put("sapphire", sapphire);
	        
	        Material ruby = new Material("ruby",TextFormatting.RED);
	        ruby.addTrait(BloodyMary.bloodymary);
	        ruby.addTrait(sharp,HEAD);
	        ruby.addItem("gemRuby", 1, Material.VALUE_Ingot);
	        ruby.setCraftable(true);
	        proxy.setRenderInfo(ruby,0xFF0000);
	        TinkerRegistry.addMaterialStats(ruby, new HeadMaterialStats(660, 4.6f, 6.4f, COBALT));
	        TinkerRegistry.addMaterialStats(ruby, new HandleMaterialStats(1.2f, 0));
	        TinkerRegistry.addMaterialStats(ruby, new ExtraMaterialStats(20));
	        TinkerRegistry.addMaterialStats(ruby, new BowMaterialStats(1.5f,1.4f,4));
	        materials.put("ruby", ruby);
	        
	        Material peridot = new Material("peridot",TextFormatting.GREEN);
	        peridot.addTrait(NaturesBlessing.naturesblessing);
	        peridot.addItem("gemPeridot", 1, Material.VALUE_Ingot);
	        peridot.setCraftable(true);
	        proxy.setRenderInfo(peridot,0xBEFA5C);
	        TinkerRegistry.addMaterialStats(peridot, new HeadMaterialStats(640, 4.0f, 6.1f, COBALT));
	        TinkerRegistry.addMaterialStats(peridot, new HandleMaterialStats(1.3f, -30));
	        TinkerRegistry.addMaterialStats(peridot, new ExtraMaterialStats(20));
	        TinkerRegistry.addMaterialStats(peridot, new BowMaterialStats(1.4f,1.4f,4));
	        materials.put("peridot", peridot);
	        
	        Material malachite = new Material("malachite",TextFormatting.DARK_GREEN);
	        malachite.addTrait(NaturesWrath.natureswrath);
	        malachite.addItem("gemMalachite", 1, Material.VALUE_Ingot);
	        malachite.setCraftable(true);
	        proxy.setRenderInfo(malachite,0x007523);
	        TinkerRegistry.addMaterialStats(malachite, new HeadMaterialStats(640, 3.0f, 6.1f, COBALT));
	        TinkerRegistry.addMaterialStats(malachite, new HandleMaterialStats(1.3f, -30));
	        TinkerRegistry.addMaterialStats(malachite, new ExtraMaterialStats(20));
	        TinkerRegistry.addMaterialStats(malachite, new BowMaterialStats(1.4f,1.4f,4));
	        materials.put("malachite", malachite);
	        
	        Material amber = new Material("amber",TextFormatting.GOLD);
	        amber.addTrait(shocking);
	        amber.addItem("gemAmber", 1, Material.VALUE_Ingot);
	        amber.setCraftable(true);
	        proxy.setRenderInfo(amber,0xFFD000);
	        TinkerRegistry.addMaterialStats(amber, new HeadMaterialStats(730, 4.6f, 5.7f, COBALT));
	        TinkerRegistry.addMaterialStats(amber, new HandleMaterialStats(1, 30));
	        TinkerRegistry.addMaterialStats(amber, new ExtraMaterialStats(100));
	        TinkerRegistry.addMaterialStats(amber, justWhy);
	        materials.put("amber", amber);
	        
	        Material topaz = new Material("topaz",TextFormatting.GOLD);
	        topaz.addTrait(NaturesPower.naturespower);
	        topaz.addItem("gemTopaz", 1, Material.VALUE_Ingot);
	        topaz.setCraftable(true);
	        proxy.setRenderInfo(topaz,0xFFFF00);
	        TinkerRegistry.addMaterialStats(topaz, new HeadMaterialStats(690, 6, 6, COBALT));
	        TinkerRegistry.addMaterialStats(topaz, new HandleMaterialStats(0.8f, 70));
	        TinkerRegistry.addMaterialStats(topaz, new ExtraMaterialStats(65));
	        TinkerRegistry.addMaterialStats(topaz, new BowMaterialStats(0.4f,1.4f,7));
	        materials.put("topaz", topaz);
	        
	        Material tanzanite = new Material("tanzanite",TextFormatting.LIGHT_PURPLE);
	        tanzanite.addTrait(freezing);
	        tanzanite.addItem("gemTanzanite", 1, Material.VALUE_Ingot);
	        tanzanite.setCraftable(true);
	        proxy.setRenderInfo(tanzanite,0x6200FF);
	        TinkerRegistry.addMaterialStats(tanzanite, new HeadMaterialStats(650, 3, 7, COBALT));
	        TinkerRegistry.addMaterialStats(tanzanite, new HandleMaterialStats(0.7f, 0));
	        TinkerRegistry.addMaterialStats(tanzanite, new ExtraMaterialStats(25));
	        TinkerRegistry.addMaterialStats(tanzanite, justWhy);
	        materials.put("tanzanite", tanzanite);
	        
	        Material amethyst = new Material("amethyst",TextFormatting.LIGHT_PURPLE);
	        amethyst.addTrait(Apocalypse.apocalypse);
	        amethyst.addItem("gemAmethyst", 1, Material.VALUE_Ingot);
	        amethyst.setCraftable(true);
	        proxy.setRenderInfo(amethyst,0xFF00FF);
	        TinkerRegistry.addMaterialStats(amethyst, new HeadMaterialStats(1200, 6, 10, COBALT));
	        TinkerRegistry.addMaterialStats(amethyst, new HandleMaterialStats(1.6f, 100));
	        TinkerRegistry.addMaterialStats(amethyst, new ExtraMaterialStats(100));
	        TinkerRegistry.addMaterialStats(amethyst, new BowMaterialStats(0.65f, 1.7f, 6.5f));
	        materials.put("amethyst", amethyst);
		}
		
		
		if (config.mekanism && Loader.isModLoaded("Mekanism")) {
			// ugly workaround for dusts not melting
			Item tinDust = new Item().setUnlocalizedName("tindust").setRegistryName("tindust");
			GameRegistry.register(tinDust);
			OreDictionary.registerOre("dustTin", tinDust);
			proxy.registerItemRenderer(tinDust, 0, "tindust");
			Item osmiumDust = new Item().setUnlocalizedName("osmiumdust").setRegistryName("osmiumdust");
			GameRegistry.register(osmiumDust);
			OreDictionary.registerOre("dustOsmium", osmiumDust);
			proxy.registerItemRenderer(osmiumDust, 0, "osmiumdust");
			Item steelDust = new Item().setUnlocalizedName("steeldust").setRegistryName("steeldust");
			GameRegistry.register(steelDust);
			OreDictionary.registerOre("dustSteel", steelDust);
			proxy.registerItemRenderer(steelDust, 0, "steeldust");
			
			// ugly workaround for molten tin not registering
			if (OreDictionary.getOres("ingotTin").size() == 0 || TinkerRegistry.getMelting(OreDictionary.getOres("ingotTin").get(0)) == null) {
				MaterialIntegration tinI = new MaterialIntegration(null, TinkerFluids.tin, "Tin");
				tinI.integrate();
				tinI.integrateRecipes();
				materialIntegrations.put("tin",tinI);
			}
			
	        Material osmium = new Material("osmium",TextFormatting.BLUE);
	        osmium.addTrait(dense);
	        osmium.addTrait(established);
	        osmium.addItem("ingotOsmium", 1, Material.VALUE_Ingot);
	        osmium.setCraftable(false).setCastable(true);
	        proxy.setRenderInfo(osmium,0xBFD0FF);
	        
	        FluidMolten osmiumFluid = Utils.fluidMetal("osmium",0xBFD0FF);
	        osmiumFluid.setTemperature(820);
	        Utils.initFluidMetal(osmiumFluid);
	        osmium.setFluid(osmiumFluid);
	        
	        TinkerRegistry.addMaterialStats(osmium, new HeadMaterialStats(500, 6, 5.8f, DIAMOND));
	        TinkerRegistry.addMaterialStats(osmium, new HandleMaterialStats(1.2f, 45));
	        TinkerRegistry.addMaterialStats(osmium, new ExtraMaterialStats(40));
	        TinkerRegistry.addMaterialStats(osmium, new BowMaterialStats(0.65f, 1.3f, 5.7f));
	        
	        materials.put("osmium", osmium);
	        
	        Material refinedObsidian = new Material("refinedObsidian",TextFormatting.LIGHT_PURPLE);
	        refinedObsidian.addTrait(dense);
	        refinedObsidian.addTrait(duritos);
	        refinedObsidian.addItem("ingotRefinedObsidian", 1, Material.VALUE_Ingot);
	        refinedObsidian.setCraftable(false).setCastable(true);
	        proxy.setRenderInfo(refinedObsidian, 0x5D00FF);
	        
	        FluidMolten refinedObsidianFluid = Utils.fluidMetal("refinedObsidian", 0x5D00FF);
	        refinedObsidianFluid.setTemperature(860);
	        Utils.initFluidMetal(refinedObsidianFluid);
	        refinedObsidian.setFluid(refinedObsidianFluid);
	        
	        TinkerRegistry.addMaterialStats(refinedObsidian, new HeadMaterialStats(2500, 7, 11, COBALT));
	        TinkerRegistry.addMaterialStats(refinedObsidian, new HandleMaterialStats(1.5f, -100));
	        TinkerRegistry.addMaterialStats(refinedObsidian, new ExtraMaterialStats(160));
	        TinkerRegistry.addMaterialStats(refinedObsidian, justWhy);
	        
	        materials.put("refinedObsidian", refinedObsidian);
		}
		
	    
	    Utils.integrate(materials,materialIntegrations);
	}
}
