package landmaster.plustic;

import java.util.*;
import landmaster.plustic.block.*;
import landmaster.plustic.proxy.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.util.*;
import landmaster.plustic.traits.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.oredict.*;
import net.minecraftforge.fml.common.Mod.*;
import slimeknights.tconstruct.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

@Mod(modid = PlusTiC.MODID, name = "PlusTiC", version = PlusTiC.VERSION, dependencies = "required-after:mantle;required-after:tconstruct;after:Mekanism;after:BiomesOPlenty;after:Botania;after:advancedRocketry;after:armorplus")
public class PlusTiC {
	public static final String MODID = "plustic";
	public static final String VERSION = "2.1";
	
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
		
		if (config.base) {
			Material tnt = new Material("tnt",TextFormatting.RED);
			tnt.addTrait(Explosive.explosive);
			tnt.addItem(Blocks.TNT, Material.VALUE_Ingot);
			tnt.setCraftable(true);
			proxy.setRenderInfo(tnt, 0xFF4F4F);
			TinkerRegistry.addMaterialStats(tnt, new ArrowShaftMaterialStats(0.95f, 0));
			materials.put("tnt", tnt);
			
			if (TinkerIntegration.isIntegrated(TinkerFluids.aluminum)) {
				// alumite is back! (with some changes)
				Item alumiteIngot = new Item().setUnlocalizedName("alumiteingot")
						.setRegistryName("alumiteingot");
				GameRegistry.register(alumiteIngot);
				OreDictionary.registerOre("ingotAlumite", alumiteIngot);
				proxy.registerItemRenderer(alumiteIngot, 0, "alumiteingot");
				
				Item alumiteNugget = new Item().setUnlocalizedName("alumitenugget")
						.setRegistryName("alumitenugget");
				GameRegistry.register(alumiteNugget);
				OreDictionary.registerOre("nuggetAlumite", alumiteNugget);
				proxy.registerItemRenderer(alumiteNugget, 0, "alumitenugget");
				
				Block alumiteBlock = new MetalBlock("alumiteblock");
				ItemBlock alumiteBlock_item = new ItemBlock(alumiteBlock);
				GameRegistry.register(alumiteBlock);
				GameRegistry.register(alumiteBlock_item, alumiteBlock.getRegistryName());
				OreDictionary.registerOre("blockAlumite", alumiteBlock);
				proxy.registerItemRenderer(alumiteBlock_item, 0, "alumiteblock");
				
				Material alumite = new Material("alumite", TextFormatting.RED);
				alumite.addTrait(Global.global);
				alumite.addItem("ingotAlumite", 1, Material.VALUE_Ingot);
				alumite.setCraftable(false).setCastable(true);
				proxy.setRenderInfo(alumite, 0xFFE0F1);
				
				FluidMolten alumiteFluid = Utils.fluidMetal("alumite", 0xFFE0F1);
				alumiteFluid.setTemperature(890);
				Utils.initFluidMetal(alumiteFluid);
				alumite.setFluid(alumiteFluid);
				TinkerRegistry.registerAlloy(new FluidStack(alumiteFluid, 3),
						new FluidStack(TinkerFluids.aluminum, 5),
						new FluidStack(TinkerFluids.iron, 2),
						new FluidStack(TinkerFluids.obsidian, 2));
				
				TinkerRegistry.addMaterialStats(alumite,
						new HeadMaterialStats(700, 6.8f, 5.5f, COBALT),
						new HandleMaterialStats(1.10f, 70),
						new ExtraMaterialStats(80),
						new BowMaterialStats(0.65f, 1.6f, 7f));
				
				materials.put("alumite", alumite);
			}
		}
		
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
	        
	        Material malachite = new Material("malachite_gem",TextFormatting.DARK_GREEN);
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
	        amber.addTrait(Thundering.thundering, PROJECTILE);
	        amber.addTrait(Thundering.thundering, SHAFT);
	        amber.addItem("gemAmber", 1, Material.VALUE_Ingot);
	        amber.setCraftable(true);
	        proxy.setRenderInfo(amber,0xFFD000);
	        TinkerRegistry.addMaterialStats(amber, new HeadMaterialStats(730, 4.6f, 5.7f, COBALT));
	        TinkerRegistry.addMaterialStats(amber, new HandleMaterialStats(1, 30));
	        TinkerRegistry.addMaterialStats(amber, new ExtraMaterialStats(100));
	        TinkerRegistry.addMaterialStats(amber, justWhy);
	        TinkerRegistry.addMaterialStats(amber, new ArrowShaftMaterialStats(1, 5));
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
			Item bronzeNugget = new Item().setUnlocalizedName("bronzenugget").setRegistryName("bronzenugget");
			GameRegistry.register(bronzeNugget);
			OreDictionary.registerOre("nuggetBronze", bronzeNugget);
			proxy.registerItemRenderer(bronzeNugget, 0, "bronzenugget");
			Item bronzeIngot = new Item().setUnlocalizedName("bronzeingot").setRegistryName("bronzeingot");
			GameRegistry.register(bronzeIngot);
			OreDictionary.registerOre("ingotBronze", bronzeIngot);
			proxy.registerItemRenderer(bronzeIngot, 0, "bronzeingot");
			
			// ugly workaround for molten tin not registering
			if (OreDictionary.getOres("ingotTin").size() == 0 || TinkerRegistry.getMelting(OreDictionary.getOres("ingotTin").get(0)) == null) {
				MaterialIntegration tinI = new MaterialIntegration(null, TinkerFluids.tin, "Tin");
				tinI.integrate();
				tinI.integrateRecipes();
				materialIntegrations.put("tin", tinI);
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
		
		if (config.botania && Loader.isModLoaded("Botania")) {
			Material terrasteel = new Material("terrasteel",TextFormatting.GREEN);
	        terrasteel.addTrait(Mana.mana);
	        terrasteel.addTrait(Terrafirma.terrafirma[0]);
	        terrasteel.addTrait(Terrafirma.terrafirma[1],HEAD);
	        terrasteel.addItem("ingotTerrasteel", 1, Material.VALUE_Ingot);
	        terrasteel.setCraftable(false).setCastable(true);
	        proxy.setRenderInfo(terrasteel, 0x00FF00);
	        
	        FluidMolten terrasteelFluid = Utils.fluidMetal("terrasteel", 0x00FF00);
	        terrasteelFluid.setTemperature(760);
	        Utils.initFluidMetal(terrasteelFluid);
	        terrasteel.setFluid(terrasteelFluid);
	        
	        TinkerRegistry.addMaterialStats(terrasteel, new HeadMaterialStats(1562, 9, 5, OBSIDIAN));
	        TinkerRegistry.addMaterialStats(terrasteel, new HandleMaterialStats(1f, 10));
	        TinkerRegistry.addMaterialStats(terrasteel, new ExtraMaterialStats(10));
	        TinkerRegistry.addMaterialStats(terrasteel, new BowMaterialStats(0.4f, 2f, 9f));
	        
	        materials.put("terrasteel", terrasteel);
	        
	        Material elementium = new Material("elementium",TextFormatting.LIGHT_PURPLE);
	        elementium.addTrait(Mana.mana);
	        elementium.addTrait(Elemental.elemental,HEAD);
	        elementium.addItem("ingotElvenElementium", 1, Material.VALUE_Ingot);
	        elementium.setCraftable(false).setCastable(true);
	        proxy.setRenderInfo(elementium, 0xF66AFD);
	        
	        FluidMolten elementiumFluid = Utils.fluidMetal("elementium", 0xF66AFD);
	        elementiumFluid.setTemperature(800);
	        Utils.initFluidMetal(elementiumFluid);
	        elementium.setFluid(elementiumFluid);
	        
	        TinkerRegistry.addMaterialStats(elementium,
                    new HeadMaterialStats(204, 6.00f, 4.00f, DIAMOND),
                    new HandleMaterialStats(0.85f, 60),
                    new ExtraMaterialStats(50));
	        TinkerRegistry.addMaterialStats(elementium, new BowMaterialStats(0.5f, 1.5f, 7f));
	        
	        materials.put("elvenElementium", elementium);
	        
	        Material manasteel = new Material("manasteel",TextFormatting.BLUE);
	        manasteel.addTrait(Mana.mana);
	        manasteel.addItem("ingotManasteel", 1, Material.VALUE_Ingot);
	        manasteel.setCraftable(false).setCastable(true);
	        proxy.setRenderInfo(manasteel, 0x54E5FF);
	        
	        FluidMolten manasteelFluid = Utils.fluidMetal("manasteel", 0x54E5FF);
	        manasteelFluid.setTemperature(681);
	        Utils.initFluidMetal(manasteelFluid);
	        manasteel.setFluid(manasteelFluid);
	        
	        TinkerRegistry.addMaterialStats(manasteel,
                    new HeadMaterialStats(540, 7.00f, 6.00f, OBSIDIAN),
                    new HandleMaterialStats(1.25f, 150),
                    new ExtraMaterialStats(60));
	        TinkerRegistry.addMaterialStats(manasteel, new BowMaterialStats(1, 1.1f, 1));
	        
	        materials.put("manasteel", manasteel);
		}
		
		if (config.advancedRocketry && Loader.isModLoaded("advancedRocketry")) {
			Material iridium = new Material("iridium", TextFormatting.GRAY);
			iridium.addTrait(dense);
			iridium.addTrait(alien, HEAD);
			iridium.addItem("ingotIridium", 1, Material.VALUE_Ingot);
			iridium.setCraftable(false).setCastable(true);
			proxy.setRenderInfo(iridium, 0xE5E5E5);
			
			FluidMolten iridiumFluid = Utils.fluidMetal("iridium", 0xE5E5E5);
			iridiumFluid.setTemperature(810);
			Utils.initFluidMetal(iridiumFluid);
			iridium.setFluid(iridiumFluid);
			
			TinkerRegistry.addMaterialStats(iridium, new HeadMaterialStats(520, 6, 5.8f, DIAMOND));
	        TinkerRegistry.addMaterialStats(iridium, new HandleMaterialStats(1.15f, -20));
	        TinkerRegistry.addMaterialStats(iridium, new ExtraMaterialStats(60));
	        TinkerRegistry.addMaterialStats(iridium, justWhy);
	        
	        materials.put("iridium", iridium);
	        
	        Material titanium = new Material("titanium", TextFormatting.WHITE);
	        titanium.addTrait(Light.light);
	        titanium.addTrait(Anticorrosion.anticorrosion, HEAD);
	        titanium.addItem("ingotTitanium", 1, Material.VALUE_Ingot);
	        titanium.setCraftable(false).setCastable(true);
	        proxy.setRenderInfo(titanium, 0xDCE1EA);
	        
	        FluidMolten titaniumFluid = Utils.fluidMetal("titanium", 0xDCE1EA);
	        titaniumFluid.setTemperature(790);
	        Utils.initFluidMetal(titaniumFluid);
	        titanium.setFluid(titaniumFluid);
	        
	        TinkerRegistry.addMaterialStats(titanium, new HeadMaterialStats(560, 6, 6, OBSIDIAN));
	        TinkerRegistry.addMaterialStats(titanium, new HandleMaterialStats(1.4f, 0));
	        TinkerRegistry.addMaterialStats(titanium, new ExtraMaterialStats(40));
	        TinkerRegistry.addMaterialStats(titanium, new BowMaterialStats(1.15f, 1.3f, 6.6f));
	        TinkerRegistry.addMaterialStats(titanium, new FletchingMaterialStats(1.0f, 1.3f));
	        
	        materials.put("titanium", titanium);
	        
	        
	        if (config.mekanism && Loader.isModLoaded("Mekanism")) {
	        	// osmiridium
	        	Item osmiridiumIngot = new Item().setUnlocalizedName("osmiridiumingot")
	        			.setRegistryName("osmiridiumingot");
	        	GameRegistry.register(osmiridiumIngot);
	        	OreDictionary.registerOre("ingotOsmiridium", osmiridiumIngot);
	        	proxy.registerItemRenderer(osmiridiumIngot, 0, "osmiridiumingot");
	        	
	        	Item osmiridiumNugget = new Item().setUnlocalizedName("osmiridiumnugget")
	        			.setRegistryName("osmiridiumnugget");
	        	GameRegistry.register(osmiridiumNugget);
	        	OreDictionary.registerOre("nuggetOsmiridium", osmiridiumNugget);
	        	proxy.registerItemRenderer(osmiridiumNugget, 0, "osmiridiumnugget");
	        	
	        	MetalBlock osmiridiumBlock = new MetalBlock("osmiridiumblock");
	        	ItemBlock osmiridiumBlock_item = new ItemBlock(osmiridiumBlock);
	        	GameRegistry.register(osmiridiumBlock);
	        	GameRegistry.register(osmiridiumBlock_item, osmiridiumBlock.getRegistryName());
	        	OreDictionary.registerOre("blockOsmiridium", osmiridiumBlock);
	        	proxy.registerItemRenderer(osmiridiumBlock_item, 0, "osmiridiumblock");
	        	
	        	Material osmiridium = new Material("osmiridium", TextFormatting.LIGHT_PURPLE);
	        	osmiridium.addTrait(DevilsStrength.devilsstrength);
	        	osmiridium.addTrait(Anticorrosion.anticorrosion, HEAD);
	        	osmiridium.addItem("ingotOsmiridium", 1, Material.VALUE_Ingot);
	        	osmiridium.setCraftable(false).setCastable(true);
	        	proxy.setRenderInfo(osmiridium, 0x666DFF);
	        	
	        	FluidMolten osmiridiumFluid = Utils.fluidMetal("osmiridium", 0x666DFF);
	        	osmiridiumFluid.setTemperature(840);
	        	Utils.initFluidMetal(osmiridiumFluid);
	        	osmiridium.setFluid(osmiridiumFluid);
	        	TinkerRegistry.registerAlloy(new FluidStack(osmiridiumFluid, 2),
	        			new FluidStack(materials.get("osmium").getFluid(), 1),
	        			new FluidStack(iridiumFluid, 1));
	        	
	        	TinkerRegistry.addMaterialStats(osmiridium, new HeadMaterialStats(1300, 6.8f, 8, COBALT));
	        	TinkerRegistry.addMaterialStats(osmiridium, new HandleMaterialStats(1.5f, 30));
	        	TinkerRegistry.addMaterialStats(osmiridium, new ExtraMaterialStats(80));
	        	TinkerRegistry.addMaterialStats(osmiridium, new BowMaterialStats(0.38f, 2.05f, 10));
	        	
	        	materials.put("osmiridium", osmiridium);
	        }
		}
		
		if (config.armorPlus && Loader.isModLoaded("armorplus")) {
			Material witherBone = new Material("witherbone", TextFormatting.BLACK);
			witherBone.addTrait(Apocalypse.apocalypse);
			witherBone.addItem("witherBone", 1, Material.VALUE_Ingot);
			witherBone.setCraftable(true);
			proxy.setRenderInfo(witherBone, 0x000000);
			TinkerRegistry.addMaterialStats(witherBone, new ArrowShaftMaterialStats(1.0f, 20));
			materials.put("witherbone", witherBone);
			
			Material guardianScale = new Material("guardianscale", TextFormatting.AQUA);
			guardianScale.addTrait(DivineShield.divineShield, HEAD);
			guardianScale.addTrait(aquadynamic);
			guardianScale.addItem("scaleGuardian", 1, Material.VALUE_Ingot);
			guardianScale.setCraftable(true);
			proxy.setRenderInfo(guardianScale, 0x00FFFF);
			TinkerRegistry.addMaterialStats(guardianScale, new HeadMaterialStats(600, 6.2f, 7, COBALT));
			TinkerRegistry.addMaterialStats(guardianScale, new HandleMaterialStats(0.9f, 40));
			TinkerRegistry.addMaterialStats(guardianScale, new ExtraMaterialStats(80));
			TinkerRegistry.addMaterialStats(guardianScale, new BowMaterialStats(0.85f, 1.2f, 5.5f));
			materials.put("guardianscale", guardianScale);
		}
		
	    
	    Utils.integrate(materials,materialIntegrations);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Item bronzeNugget = Item.REGISTRY.getObject(new ResourceLocation(MODID, "bronzenugget"));
		Item bronzeIngot = Item.REGISTRY.getObject(new ResourceLocation(MODID, "bronzeingot"));
		Block osmiridiumBlock = Block.REGISTRY.getObject(new ResourceLocation(MODID, "osmiridiumblock"));
		Item osmiridiumIngot = Item.REGISTRY.getObject(new ResourceLocation(MODID, "osmiridiumingot"));
		Item osmiridiumNugget = Item.REGISTRY.getObject(new ResourceLocation(MODID, "osmiridiumnugget"));
		Block alumiteBlock = Block.REGISTRY.getObject(new ResourceLocation(MODID, "alumiteblock"));
		Item alumiteIngot = Item.REGISTRY.getObject(new ResourceLocation(MODID, "alumiteingot"));
		Item alumiteNugget = Item.REGISTRY.getObject(new ResourceLocation(MODID, "alumitenugget"));
		if (bronzeNugget != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(bronzeIngot),
					"III", "III", "III",
					'I', "nuggetBronze"));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(bronzeNugget, 9), "ingotBronze"));
		}
		if (osmiridiumNugget != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(osmiridiumBlock),
					"III", "III", "III",
					'I', "ingotOsmiridium"));
			GameRegistry.addShapelessRecipe(new ItemStack(osmiridiumIngot, 9), osmiridiumBlock);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(osmiridiumIngot),
					"III", "III", "III",
					'I', "nuggetOsmiridium"));
			GameRegistry.addShapelessRecipe(new ItemStack(osmiridiumNugget, 9), osmiridiumIngot);
		}
		if (alumiteNugget != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(alumiteBlock),
					"III", "III", "III",
					'I', "ingotAlumite"));
			GameRegistry.addShapelessRecipe(new ItemStack(alumiteIngot, 9), alumiteBlock);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(alumiteIngot),
					"III", "III", "III",
					'I', "nuggetAlumite"));
			GameRegistry.addShapelessRecipe(new ItemStack(alumiteNugget, 9), alumiteIngot);
		}
	}
}
