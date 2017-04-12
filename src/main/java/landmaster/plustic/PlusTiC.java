package landmaster.plustic;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;

import com.brandon3055.draconicevolution.DEFeatures;
import com.progwml6.natura.shared.*;

import landmaster.plustic.proxy.*;
import landmaster.plustic.tools.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.modules.*;
import landmaster.plustic.net.*;
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

@Mod(modid = PlusTiC.MODID, name = PlusTiC.NAME, version = PlusTiC.VERSION, dependencies = PlusTiC.DEPENDS)
public class PlusTiC {
	public static final String MODID = "plustic";
	public static final String NAME = "PlusTiC";
	public static final String VERSION = "4.2.0.1";
	public static final String DEPENDS = "required-after:mantle;"
			+ "required-after:tconstruct;required-after:CodeChickenLib;"
			+ "after:Mekanism;after:BiomesOPlenty;after:Botania;after:advancedRocketry;"
			+ "after:armorplus;after:EnderIO;after:projectred-exploration;"
			+ "after:thermalfoundation;after:substratum;after:draconicevolution;"
			+ "after:landcore;after:tesla;after:baubles;after:actuallyadditions;"
			+ "after:natura;after:Psi";
	
	public static Config config;
	
	@Mod.Instance(PlusTiC.MODID)
	public static PlusTiC INSTANCE;
	
	@SidedProxy(serverSide = "landmaster.plustic.proxy.CommonProxy", clientSide = "landmaster.plustic.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static final Logger log = LogManager.getLogger(
			MODID.toUpperCase(Locale.US/* to avoid problems with Turkish */));
	
	public static final Map<String, Material> materials = new HashMap<>();
	public static final Map<String, MaterialIntegration> materialIntegrations = new HashMap<>();
	
	public static final BowMaterialStats justWhy = new BowMaterialStats(0.2f, 0.4f, -1f);
	
	public static ToolKatana katana;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		(config = new Config(event)).sync();
		
		proxy.initEntities();
		
		initTools();
		initBase();
		initBoP();
		initMekanism();
		initBotania();
		initAdvRocketry();
		initArmorPlus();
		initEnderIO();
		initTF();
		initDraconicEvolution();
		initActAdd();
		initNatura();
		initPsi();
		
		integrate(materials, materialIntegrations);
		Utils.registerModifiers();
	}
	
	private static void initTools() {
		if (Config.katana) {
			katana = new ToolKatana();
			GameRegistry.register(katana);
			TinkerRegistry.registerToolForgeCrafting(katana);
			proxy.registerToolModel(katana);
		}
	}
	
	private static void initBase() {
		if (Config.base) {
			Material tnt = new Material("tnt", TextFormatting.RED);
			tnt.addTrait(Explosive.explosive);
			tnt.addItem(Blocks.TNT, Material.VALUE_Ingot);
			tnt.setRepresentativeItem(Blocks.TNT);
			tnt.setCraftable(true);
			proxy.setRenderInfo(tnt, 0xFF4F4F);
			TinkerRegistry.addMaterialStats(tnt, new ArrowShaftMaterialStats(0.95f, 0));
			materials.put("tnt", tnt);
			
			Material emerald = new Material("emerald_plustic", TextFormatting.GREEN);
			emerald.addTrait(Terrafirma.terrafirma.get(0));
			emerald.addTrait(Elemental.elemental, HEAD);
			emerald.addItem("gemEmerald", 1, Material.VALUE_Ingot);
			emerald.setRepresentativeItem(Items.EMERALD);
			emerald.setCraftable(false).setCastable(true);
			proxy.setRenderInfo(emerald, 0x13DB52);
			emerald.setFluid(TinkerFluids.emerald);
			TinkerRegistry.addMaterialStats(emerald, new HeadMaterialStats(1222, 7, 7, COBALT),
					new HandleMaterialStats(1.1f, 0),
					new ExtraMaterialStats(70),
					new BowMaterialStats(1.1f, 1, 0.9f));
			materials.put("emerald", emerald);
			
			if (TinkerIntegration.isIntegrated(TinkerFluids.aluminum)) {
				// alumite is back! (with some changes)
				Utils.ItemMatGroup alumiteGroup = Utils.registerMatGroup("alumite");
				
				Material alumite = new Material("alumite", TextFormatting.RED);
				alumite.addTrait(Global.global);
				alumite.addItem("ingotAlumite", 1, Material.VALUE_Ingot);
				alumite.setCraftable(false).setCastable(true);
				alumite.setRepresentativeItem(alumiteGroup.ingot);
				proxy.setRenderInfo(alumite, 0xFFE0F1);
				
				FluidMolten alumiteFluid = Utils.fluidMetal("alumite", 0xFFE0F1);
				alumiteFluid.setTemperature(890);
				Utils.initFluidMetal(alumiteFluid);
				alumite.setFluid(alumiteFluid);
				TinkerRegistry.registerAlloy(new FluidStack(alumiteFluid, 3), new FluidStack(TinkerFluids.aluminum, 5),
						new FluidStack(TinkerFluids.iron, 2), new FluidStack(TinkerFluids.obsidian, 2));
				
				TinkerRegistry.addMaterialStats(alumite, new HeadMaterialStats(700, 6.8f, 5.5f, COBALT),
						new HandleMaterialStats(1.10f, 70), new ExtraMaterialStats(80),
						new BowMaterialStats(0.65f, 1.6f, 7f));
				
				materials.put("alumite", alumite);
			}
			
			if (TinkerIntegration.isIntegrated(TinkerFluids.nickel)) {
				Material nickel = new Material("nickel", TextFormatting.YELLOW);
				nickel.addTrait(NickOfTime.nickOfTime, HEAD);
				nickel.addTrait(magnetic);
				nickel.addItem("ingotNickel", 1, Material.VALUE_Ingot);
				nickel.setCraftable(false).setCastable(true);
				Utils.setDispItem(nickel, "ingotNickel");
				proxy.setRenderInfo(nickel, 0xFFF98E);
				
				nickel.setFluid(TinkerFluids.nickel);
				
				TinkerRegistry.addMaterialStats(nickel, new HeadMaterialStats(460, 6, 4.5f, OBSIDIAN),
						new HandleMaterialStats(1, -5), new ExtraMaterialStats(70), justWhy,
						new FletchingMaterialStats(0.95f, 1.05f));
				
				materials.put("nickel", nickel);
			}
		}
	}
	
	private static void initBoP() {
		if ((Config.bop && Loader.isModLoaded("BiomesOPlenty"))
				|| (Config.projectRed && Loader.isModLoaded("projectred-exploration"))) {
			Material sapphire = new Material("sapphire", TextFormatting.BLUE);
			sapphire.addTrait(aquadynamic);
			sapphire.addItem("gemSapphire", 1, Material.VALUE_Ingot);
			sapphire.setCraftable(true);
			// sapphire.setRepresentativeItem(OreDictionary.getOres("gemSapphire").get(0));
			proxy.setRenderInfo(sapphire, 0x0000FF);
			TinkerRegistry.addMaterialStats(sapphire, new HeadMaterialStats(700, 5, 6.4f, COBALT));
			TinkerRegistry.addMaterialStats(sapphire, new HandleMaterialStats(1, 100));
			TinkerRegistry.addMaterialStats(sapphire, new ExtraMaterialStats(120));
			TinkerRegistry.addMaterialStats(sapphire, new BowMaterialStats(1, 1.5f, 4));
			materials.put("sapphire", sapphire);
			
			Material ruby = new Material("ruby", TextFormatting.RED);
			ruby.addTrait(BloodyMary.bloodymary);
			ruby.addTrait(sharp, HEAD);
			ruby.addItem("gemRuby", 1, Material.VALUE_Ingot);
			ruby.setCraftable(true);
			// ruby.setRepresentativeItem(OreDictionary.getOres("gemRuby").get(0));
			proxy.setRenderInfo(ruby, 0xFF0000);
			TinkerRegistry.addMaterialStats(ruby, new HeadMaterialStats(660, 4.6f, 6.4f, COBALT));
			TinkerRegistry.addMaterialStats(ruby, new HandleMaterialStats(1.2f, 0));
			TinkerRegistry.addMaterialStats(ruby, new ExtraMaterialStats(20));
			TinkerRegistry.addMaterialStats(ruby, new BowMaterialStats(1.5f, 1.4f, 4));
			materials.put("ruby", ruby);
			
			Material peridot = new Material("peridot", TextFormatting.GREEN);
			peridot.addTrait(NaturesBlessing.naturesblessing);
			peridot.addItem("gemPeridot", 1, Material.VALUE_Ingot);
			peridot.setCraftable(true);
			// peridot.setRepresentativeItem(OreDictionary.getOres("gemPeridot").get(0));
			proxy.setRenderInfo(peridot, 0xBEFA5C);
			TinkerRegistry.addMaterialStats(peridot, new HeadMaterialStats(640, 4.0f, 6.1f, COBALT));
			TinkerRegistry.addMaterialStats(peridot, new HandleMaterialStats(1.3f, -30));
			TinkerRegistry.addMaterialStats(peridot, new ExtraMaterialStats(20));
			TinkerRegistry.addMaterialStats(peridot, new BowMaterialStats(1.4f, 1.4f, 4));
			materials.put("peridot", peridot);
		}
		if (Config.bop && Loader.isModLoaded("BiomesOPlenty")) {
			Material malachite = new Material("malachite_gem", TextFormatting.DARK_GREEN);
			malachite.addTrait(NaturesWrath.natureswrath);
			malachite.addItem("gemMalachite", 1, Material.VALUE_Ingot);
			malachite.setCraftable(true);
			Utils.setDispItem(malachite, "biomesoplenty", "gem", 5);
			proxy.setRenderInfo(malachite, 0x007523);
			TinkerRegistry.addMaterialStats(malachite, new HeadMaterialStats(640, 3.0f, 6.1f, COBALT));
			TinkerRegistry.addMaterialStats(malachite, new HandleMaterialStats(1.3f, -30));
			TinkerRegistry.addMaterialStats(malachite, new ExtraMaterialStats(20));
			TinkerRegistry.addMaterialStats(malachite, new BowMaterialStats(1.4f, 1.4f, 4));
			materials.put("malachite", malachite);
			
			Material amber = new Material("amber", TextFormatting.GOLD);
			amber.addTrait(shocking);
			amber.addTrait(Thundering.thundering, PROJECTILE);
			amber.addTrait(Thundering.thundering, SHAFT);
			amber.addItem("gemAmber", 1, Material.VALUE_Ingot);
			amber.setCraftable(true);
			Utils.setDispItem(amber, "biomesoplenty", "gem", 7);
			proxy.setRenderInfo(amber, 0xFFD000);
			TinkerRegistry.addMaterialStats(amber, new HeadMaterialStats(730, 4.6f, 5.7f, COBALT));
			TinkerRegistry.addMaterialStats(amber, new HandleMaterialStats(1, 30));
			TinkerRegistry.addMaterialStats(amber, new ExtraMaterialStats(100));
			TinkerRegistry.addMaterialStats(amber, justWhy);
			TinkerRegistry.addMaterialStats(amber, new ArrowShaftMaterialStats(1, 5));
			materials.put("amber", amber);
			
			Material topaz = new Material("topaz", TextFormatting.GOLD);
			topaz.addTrait(NaturesPower.naturespower);
			topaz.addItem("gemTopaz", 1, Material.VALUE_Ingot);
			topaz.setCraftable(true);
			Utils.setDispItem(topaz, "biomesoplenty", "gem", 3);
			proxy.setRenderInfo(topaz, 0xFFFF00);
			TinkerRegistry.addMaterialStats(topaz, new HeadMaterialStats(690, 6, 6, COBALT));
			TinkerRegistry.addMaterialStats(topaz, new HandleMaterialStats(0.8f, 70));
			TinkerRegistry.addMaterialStats(topaz, new ExtraMaterialStats(65));
			TinkerRegistry.addMaterialStats(topaz, new BowMaterialStats(0.4f, 1.4f, 7));
			materials.put("topaz", topaz);
			
			Material tanzanite = new Material("tanzanite", TextFormatting.LIGHT_PURPLE);
			tanzanite.addTrait(freezing);
			tanzanite.addItem("gemTanzanite", 1, Material.VALUE_Ingot);
			tanzanite.setCraftable(true);
			Utils.setDispItem(tanzanite, "biomesoplenty", "gem", 4);
			proxy.setRenderInfo(tanzanite, 0x6200FF);
			TinkerRegistry.addMaterialStats(tanzanite, new HeadMaterialStats(650, 3, 7, COBALT));
			TinkerRegistry.addMaterialStats(tanzanite, new HandleMaterialStats(0.7f, 0));
			TinkerRegistry.addMaterialStats(tanzanite, new ExtraMaterialStats(25));
			TinkerRegistry.addMaterialStats(tanzanite, justWhy);
			materials.put("tanzanite", tanzanite);
			
			Material amethyst = new Material("amethyst", TextFormatting.LIGHT_PURPLE);
			amethyst.addTrait(Apocalypse.apocalypse);
			amethyst.addItem(Item.REGISTRY.getObject(new ResourceLocation("biomesoplenty", "gem")), 1,
					Material.VALUE_Ingot);
			amethyst.setCraftable(true);
			Utils.setDispItem(amethyst, "biomesoplenty", "gem");
			proxy.setRenderInfo(amethyst, 0xFF00FF);
			TinkerRegistry.addMaterialStats(amethyst, new HeadMaterialStats(1200, 6, 10, COBALT));
			TinkerRegistry.addMaterialStats(amethyst, new HandleMaterialStats(1.6f, 100));
			TinkerRegistry.addMaterialStats(amethyst, new ExtraMaterialStats(100));
			TinkerRegistry.addMaterialStats(amethyst, new BowMaterialStats(0.65f, 1.7f, 6.5f));
			materials.put("amethyst", amethyst);
		}
	}
	
	private static void initMekanism() {
		if (Config.mekanism && Loader.isModLoaded("Mekanism")) {
			// ugly workaround for dusts not melting
			Item tinDust = new Item().setUnlocalizedName("tindust").setRegistryName("tindust");
			tinDust.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(tinDust);
			OreDictionary.registerOre("dustTin", tinDust);
			proxy.registerItemRenderer(tinDust, 0, "tindust");
			Item osmiumDust = new Item().setUnlocalizedName("osmiumdust").setRegistryName("osmiumdust");
			osmiumDust.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(osmiumDust);
			OreDictionary.registerOre("dustOsmium", osmiumDust);
			proxy.registerItemRenderer(osmiumDust, 0, "osmiumdust");
			Item steelDust = new Item().setUnlocalizedName("steeldust").setRegistryName("steeldust");
			steelDust.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(steelDust);
			OreDictionary.registerOre("dustSteel", steelDust);
			proxy.registerItemRenderer(steelDust, 0, "steeldust");
			
			
			Item bronzeNugget = new Item().setUnlocalizedName("bronzenugget").setRegistryName("bronzenugget");
			bronzeNugget.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(bronzeNugget);
			OreDictionary.registerOre("nuggetBronze", bronzeNugget);
			proxy.registerItemRenderer(bronzeNugget, 0, "bronzenugget");
			Item bronzeIngot = new Item().setUnlocalizedName("bronzeingot").setRegistryName("bronzeingot");
			bronzeIngot.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(bronzeIngot);
			OreDictionary.registerOre("ingotBronze", bronzeIngot);
			proxy.registerItemRenderer(bronzeIngot, 0, "bronzeingot");
			
			
			// ugly workaround for molten tin not registering
			if (OreDictionary.getOres("ingotTin").size() == 0
					|| TinkerRegistry.getMelting(OreDictionary.getOres("ingotTin").get(0)) == null) {
				MaterialIntegration tinI = new MaterialIntegration(null, TinkerFluids.tin, "Tin");
				tinI.integrate();
				tinI.integrateRecipes();
				materialIntegrations.put("tin", tinI);
			}
			
			Material osmium = new Material("osmium", TextFormatting.BLUE);
			osmium.addTrait(dense);
			osmium.addTrait(established);
			osmium.addItem("ingotOsmium", 1, Material.VALUE_Ingot);
			osmium.setCraftable(false).setCastable(true);
			proxy.setRenderInfo(osmium, 0xBFD0FF);
			
			FluidMolten osmiumFluid = Utils.fluidMetal("osmium", 0xBFD0FF);
			osmiumFluid.setTemperature(820);
			Utils.initFluidMetal(osmiumFluid);
			osmium.setFluid(osmiumFluid);
			
			TinkerRegistry.addMaterialStats(osmium, new HeadMaterialStats(500, 6, 5.8f, DIAMOND));
			TinkerRegistry.addMaterialStats(osmium, new HandleMaterialStats(1.2f, 45));
			TinkerRegistry.addMaterialStats(osmium, new ExtraMaterialStats(40));
			TinkerRegistry.addMaterialStats(osmium, new BowMaterialStats(0.65f, 1.3f, 5.7f));
			
			materials.put("osmium", osmium);
			
			Material refinedObsidian = new Material("refinedObsidian", TextFormatting.LIGHT_PURPLE);
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
	}
	
	private static void initBotania() {
		if (Config.botania && Loader.isModLoaded("Botania")) {
			Material terrasteel = new Material("terrasteel", TextFormatting.GREEN);
			terrasteel.addTrait(Mana.mana);
			terrasteel.addTrait(Terrafirma.terrafirma.get(0));
			terrasteel.addTrait(Mana.mana, HEAD);
			terrasteel.addTrait(Terrafirma.terrafirma.get(1), HEAD);
			terrasteel.addItem("ingotTerrasteel", 1, Material.VALUE_Ingot);
			terrasteel.setCraftable(false).setCastable(true);
			Utils.setDispItem(terrasteel, "botania", "manaResource", 4);
			proxy.setRenderInfo(terrasteel, 0x00FF00);
			
			FluidMolten terrasteelFluid = Utils.fluidMetal("terrasteel", 0x00FF00);
			terrasteelFluid.setTemperature(760);
			Utils.initFluidMetal(terrasteelFluid);
			terrasteel.setFluid(terrasteelFluid);
			
			TinkerRegistry.addMaterialStats(terrasteel, new HeadMaterialStats(1562, 9, 6.5f, COBALT));
			TinkerRegistry.addMaterialStats(terrasteel, new HandleMaterialStats(1.4f, 10));
			TinkerRegistry.addMaterialStats(terrasteel, new ExtraMaterialStats(10));
			TinkerRegistry.addMaterialStats(terrasteel, new BowMaterialStats(0.55f, 2f, 11f));
			
			materials.put("terrasteel", terrasteel);
			
			Material elementium = new Material("elementium", TextFormatting.LIGHT_PURPLE);
			elementium.addTrait(Mana.mana);
			elementium.addTrait(Mana.mana, HEAD);
			elementium.addTrait(Elemental.elemental, HEAD);
			elementium.addItem("ingotElvenElementium", 1, Material.VALUE_Ingot);
			elementium.setCraftable(false).setCastable(true);
			Utils.setDispItem(elementium, "botania", "manaResource", 7);
			proxy.setRenderInfo(elementium, 0xF66AFD);
			
			FluidMolten elementiumFluid = Utils.fluidMetal("elementium", 0xF66AFD);
			elementiumFluid.setTemperature(800);
			Utils.initFluidMetal(elementiumFluid);
			elementium.setFluid(elementiumFluid);
			
			TinkerRegistry.addMaterialStats(elementium, new HeadMaterialStats(540, 7.00f, 6.00f, OBSIDIAN),
					new HandleMaterialStats(1.25f, 150), new ExtraMaterialStats(60));
			TinkerRegistry.addMaterialStats(elementium, new BowMaterialStats(0.8f, 1.5f, 7.5f));
			
			materials.put("elvenElementium", elementium);
			
			Material manasteel = new Material("manasteel", TextFormatting.BLUE);
			manasteel.addTrait(Mana.mana);
			manasteel.addItem("ingotManasteel", 1, Material.VALUE_Ingot);
			manasteel.setCraftable(false).setCastable(true);
			Utils.setDispItem(manasteel, "botania", "manaResource");
			proxy.setRenderInfo(manasteel, 0x54E5FF);
			
			FluidMolten manasteelFluid = Utils.fluidMetal("manasteel", 0x54E5FF);
			manasteelFluid.setTemperature(681);
			Utils.initFluidMetal(manasteelFluid);
			manasteel.setFluid(manasteelFluid);
			
			TinkerRegistry.addMaterialStats(manasteel, new HeadMaterialStats(540, 7.00f, 6.00f, OBSIDIAN),
					new HandleMaterialStats(1.25f, 150), new ExtraMaterialStats(60));
			TinkerRegistry.addMaterialStats(manasteel, new BowMaterialStats(1, 1.1f, 1));
			
			materials.put("manasteel", manasteel);
			
			Material livingwood = new Material("livingwood_plustic", TextFormatting.DARK_GREEN);
			livingwood.addTrait(Botanical.botanical.get(1), HEAD);
			livingwood.addTrait(ecological, HEAD);
			livingwood.addTrait(Botanical.botanical.get(0));
			livingwood.addTrait(ecological);
			livingwood.addItem("livingwood", 1, Material.VALUE_Ingot);
			livingwood.setCraftable(true);
			Utils.setDispItem(livingwood, "livingwood");
			proxy.setRenderInfo(livingwood, 0x560018);
			TinkerRegistry.addMaterialStats(livingwood, new HeadMaterialStats(50, 5.1f, 2.8f, IRON),
					new HandleMaterialStats(1.15f, 20), new ExtraMaterialStats(20),
					new BowMaterialStats(1.1f, 1.1f, 1.8f), new ArrowShaftMaterialStats(1f, 6));
			materials.put("livingwood", livingwood);
			
			// MIRION ALLOY
			Utils.ItemMatGroup mirionGroup = Utils.registerMatGroup("mirion");
			
			Material mirion = new Material("mirion", TextFormatting.YELLOW);
			mirion.addTrait(Mirabile.mirabile, HEAD);
			mirion.addTrait(Mana.mana, HEAD);
			mirion.addTrait(Mana.mana);
			mirion.addItem("ingotMirion", 1, Material.VALUE_Ingot);
			mirion.setCraftable(false).setCastable(true);
			mirion.setRepresentativeItem(mirionGroup.ingot);
			proxy.setRenderInfo(mirion, 0xDDFF00);
			
			FluidMolten mirionFluid = Utils.fluidMetal("mirion", 0xDDFF00);
			mirionFluid.setTemperature(777);
			Utils.initFluidMetal(mirionFluid);
			mirion.setFluid(mirionFluid);
			TinkerRegistry.registerAlloy(new FluidStack(mirionFluid, 4 * 18), new FluidStack(terrasteelFluid, 18),
					new FluidStack(manasteelFluid, 18), new FluidStack(elementiumFluid, 18),
					new FluidStack(TinkerFluids.cobalt, 18), new FluidStack(TinkerFluids.glass, 125));
			
			TinkerRegistry.addMaterialStats(mirion, new HeadMaterialStats(1919, 9, 9, 5));
			TinkerRegistry.addMaterialStats(mirion, new HandleMaterialStats(1.1f, 40));
			TinkerRegistry.addMaterialStats(mirion, new ExtraMaterialStats(90));
			TinkerRegistry.addMaterialStats(mirion, new BowMaterialStats(1.35f, 1.5f, 5.5f));
			
			materials.put("mirion", mirion);
		}
	}
	
	private static void initAdvRocketry() {
		if (Config.advancedRocketry && Loader.isModLoaded("libVulpes")) {
			Material iridium = new Material("iridium", TextFormatting.GRAY);
			iridium.addTrait(dense);
			iridium.addTrait(alien, HEAD);
			iridium.addItem("ingotIridium", 1, Material.VALUE_Ingot);
			iridium.setCraftable(false).setCastable(true);
			Utils.setDispItem(iridium, "libvulpes", "productingot", 10);
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
			Utils.setDispItem(titanium, "libvulpes", "productingot", 7);
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
			
			if (Config.mekanism && Loader.isModLoaded("Mekanism")) {
				// osmiridium
				Utils.ItemMatGroup osmiridiumGroup = Utils.registerMatGroup("osmiridium");
				
				Material osmiridium = new Material("osmiridium", TextFormatting.LIGHT_PURPLE);
				osmiridium.addTrait(DevilsStrength.devilsstrength);
				osmiridium.addTrait(Anticorrosion.anticorrosion, HEAD);
				osmiridium.addItem("ingotOsmiridium", 1, Material.VALUE_Ingot);
				osmiridium.setCraftable(false).setCastable(true);
				osmiridium.setRepresentativeItem(osmiridiumGroup.ingot);
				proxy.setRenderInfo(osmiridium, 0x666DFF);
				
				FluidMolten osmiridiumFluid = Utils.fluidMetal("osmiridium", 0x666DFF);
				osmiridiumFluid.setTemperature(840);
				Utils.initFluidMetal(osmiridiumFluid);
				osmiridium.setFluid(osmiridiumFluid);
				TinkerRegistry.registerAlloy(new FluidStack(osmiridiumFluid, 2),
						new FluidStack(materials.get("osmium").getFluid(), 1), new FluidStack(iridiumFluid, 1));
				
				TinkerRegistry.addMaterialStats(osmiridium, new HeadMaterialStats(1300, 6.8f, 8, COBALT));
				TinkerRegistry.addMaterialStats(osmiridium, new HandleMaterialStats(1.5f, 30));
				TinkerRegistry.addMaterialStats(osmiridium, new ExtraMaterialStats(80));
				TinkerRegistry.addMaterialStats(osmiridium, new BowMaterialStats(0.38f, 2.05f, 10));
				
				materials.put("osmiridium", osmiridium);
			}
		}
	}
	
	private static void initArmorPlus() {
		if (Config.armorPlus && Loader.isModLoaded("armorplus")) {
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
	}
	
	private static void initEnderIO() {
		if (Config.enderIO && Loader.isModLoaded("EnderIO")) {
			Fluid coalFluid = Utils.fluidMetal("coal", 0x111111);
			coalFluid.setTemperature(500);
			Utils.initFluidMetal(coalFluid);
			
			TinkerRegistry.registerMelting("coal", coalFluid, 100);
			TinkerRegistry.registerBasinCasting(new ItemStack(Blocks.COAL_BLOCK), null, coalFluid, 900);
			TinkerRegistry.registerTableCasting(new ItemStack(Items.COAL), null, coalFluid, 100);
			
			Material darkSteel = new Material("darksteel_plustic_enderio", TextFormatting.DARK_GRAY);
			darkSteel.addTrait(Portly.portly, HEAD);
			darkSteel.addTrait(coldblooded);
			darkSteel.addItem("ingotDarkSteel", 1, Material.VALUE_Ingot);
			darkSteel.setCraftable(false).setCastable(true);
			Utils.setDispItem(darkSteel, "enderio", "itemAlloy", 6);
			proxy.setRenderInfo(darkSteel, 0x333333);
			
			Fluid darkSteelFluid = FluidRegistry.getFluid("darksteel");
			darkSteel.setFluid(darkSteelFluid);
			
			TinkerRegistry.registerAlloy(new FluidStack(darkSteelFluid, 36), new FluidStack(TinkerFluids.obsidian, 72),
					new FluidStack(TinkerFluids.iron, 36), new FluidStack(coalFluid, 25));
			
			TinkerRegistry.addMaterialStats(darkSteel, new HeadMaterialStats(666, 7, 4, OBSIDIAN),
					new HandleMaterialStats(1.05f, 40), new ExtraMaterialStats(40),
					new BowMaterialStats(0.38f, 2.05f, 10));
			materials.put("darkSteel", darkSteel);
		}
	}
	
	private static void initTF() {
		if ((Config.thermalFoundation && Loader.isModLoaded("thermalfoundation")) || (Config.substratum && Loader.isModLoaded("substratum"))) {
			Material signalum = new Material("signalum_plustic", TextFormatting.RED);
			signalum.addTrait(BloodyMary.bloodymary);
			signalum.addItem("ingotSignalum", 1, Material.VALUE_Ingot);
			signalum.setCraftable(false).setCastable(true);
			Utils.setDispItem(signalum, "ingotSignalum");
			proxy.setRenderInfo(signalum, 0xD84100);
			
			FluidMolten signalumFluid = Utils.fluidMetal("signalum", 0xD84100);
			signalumFluid.setTemperature(930);
			Utils.initFluidMetal(signalumFluid);
			signalum.setFluid(signalumFluid);
			TinkerRegistry.registerAlloy(new FluidStack(signalumFluid, 72), new FluidStack(TinkerFluids.copper, 54),
					new FluidStack(TinkerFluids.silver, 18), new FluidStack(FluidRegistry.getFluid("redstone"), 125));
			
			TinkerRegistry.addMaterialStats(signalum, new HeadMaterialStats(690, 7.5f, 5.2f, OBSIDIAN),
					new HandleMaterialStats(1.2f, 0), new ExtraMaterialStats(55),
					new BowMaterialStats(1.2f, 1.6f, 4.4f));
			materials.put("signalum", signalum);
			
			Material platinum = new Material("platinum_plustic", TextFormatting.BLUE);
			platinum.addTrait(Global.global, HEAD);
			platinum.addTrait(Heavy.heavy);
			platinum.addTrait(Anticorrosion.anticorrosion);
			platinum.addItem("ingotPlatinum", 1, Material.VALUE_Ingot);
			platinum.setCraftable(false).setCastable(true);
			Utils.setDispItem(platinum, "ingotPlatinum");
			proxy.setRenderInfo(platinum, 0xB7E7FF);
			
			FluidMolten platinumFluid = Utils.fluidMetal("platinum", 0xB7E7FF);
			platinumFluid.setTemperature(680);
			Utils.initFluidMetal(platinumFluid);
			platinum.setFluid(platinumFluid);
			
			TinkerRegistry.addMaterialStats(platinum, new HeadMaterialStats(720, 8, 6, COBALT),
					new HandleMaterialStats(1.05f, -5),
					new ExtraMaterialStats(60),
					new BowMaterialStats(0.85f, 1.8f, 8));
			
			materials.put("platinum", platinum);
			
			Material enderium = new Material("enderium_plustic", TextFormatting.DARK_GREEN);
			enderium.addTrait(Portly.portly, HEAD);
			enderium.addTrait(Global.global);
			enderium.addTrait(enderference);
			enderium.addTrait(endspeed, PROJECTILE);
			enderium.addTrait(endspeed, SHAFT);
			enderium.addItem("ingotEnderium", 1, Material.VALUE_Ingot);
			enderium.setCraftable(false).setCastable(true);
			Utils.setDispItem(enderium, "ingotEnderium");
			proxy.setRenderInfo(enderium, 0x007068);
			
			FluidMolten enderiumFluid = Utils.fluidMetal("enderium", 0x007068);
			enderiumFluid.setTemperature(970);
			Utils.initFluidMetal(enderiumFluid);
			enderium.setFluid(enderiumFluid);
			TinkerRegistry.registerAlloy(new FluidStack(enderiumFluid, 144), new FluidStack(TinkerFluids.tin, 72),
					new FluidStack(TinkerFluids.silver, 36), new FluidStack(platinumFluid, 36),
					new FluidStack(FluidRegistry.getFluid("ender"), 250));
			
			TinkerRegistry.addMaterialStats(enderium, new HeadMaterialStats(800, 7.5f, 7, COBALT),
					new HandleMaterialStats(1.05f, -5), new ExtraMaterialStats(65), new BowMaterialStats(0.9f, 1.9f, 8),
					new ArrowShaftMaterialStats(1, 12));
			
			materials.put("enderium", enderium);
		}
	}
	
	private static void initDraconicEvolution() {
		if (Config.draconicEvolution && Loader.isModLoaded("draconicevolution")) {
			Material wyvern = new Material("wyvern_plustic", TextFormatting.DARK_PURPLE);
			wyvern.addTrait(BrownMagic.brownmagic, HEAD);
			wyvern.addTrait(BlindBandit.blindbandit, HEAD);
			wyvern.addTrait(Portly.portly);
			wyvern.addItem(DEFeatures.wyvernCore, 1, Material.VALUE_Ingot);
			wyvern.setCraftable(true);
			wyvern.setRepresentativeItem(DEFeatures.wyvernCore);
			proxy.setRenderInfo(wyvern, 0x7F00FF);
			TinkerRegistry.addMaterialStats(wyvern, new HeadMaterialStats(2000, 8, 15, 8));
			TinkerRegistry.addMaterialStats(wyvern, new HandleMaterialStats(1.6f, 130));
			TinkerRegistry.addMaterialStats(wyvern, new ExtraMaterialStats(240));
			TinkerRegistry.addMaterialStats(wyvern, new BowMaterialStats(1.6f, 2, 11));
			materials.put("wyvern_core", wyvern);
			
			Material awakened = new Material("awakened_plustic", TextFormatting.GOLD);
			awakened.addTrait(RudeAwakening.rudeawakening, HEAD);
			awakened.addTrait(BrownMagic.brownmagic, HEAD);
			awakened.addTrait(BlindBandit.blindbandit);
			awakened.addTrait(Apocalypse.apocalypse);
			awakened.addTrait(Global.global);
			awakened.addItem(DEFeatures.awakenedCore, 1, Material.VALUE_Ingot);
			awakened.setCraftable(true);
			awakened.setRepresentativeItem(DEFeatures.awakenedCore);
			proxy.setRenderInfo(awakened, 0xFFB200);
			TinkerRegistry.addMaterialStats(awakened, new HeadMaterialStats(5000, 9, 35, 10));
			TinkerRegistry.addMaterialStats(awakened, new HandleMaterialStats(1.8f, 500));
			TinkerRegistry.addMaterialStats(awakened, new ExtraMaterialStats(500));
			TinkerRegistry.addMaterialStats(awakened, new BowMaterialStats(1.9f, 2.8f, 20));
			materials.put("awakened_core", awakened);
		}
	}
	
	private static void initActAdd() {
		if (Config.actuallyAdditions && Loader.isModLoaded("actuallyadditions")) {
			Material blackQuartz = new Material("blackquartz_plustic", TextFormatting.BLACK);
			blackQuartz.addTrait(DevilsStrength.devilsstrength);
			blackQuartz.addTrait(crude2);
			blackQuartz.addItem("gemQuartzBlack", 1, Material.VALUE_Ingot);
			blackQuartz.setCraftable(true);
			proxy.setRenderInfo(blackQuartz, 0x000000);
			TinkerRegistry.addMaterialStats(blackQuartz, new HeadMaterialStats(380, 6, 4.5f, DIAMOND),
					new HandleMaterialStats(0.8f, 0),
					new ExtraMaterialStats(50),
					justWhy);
			materials.put("blackquartz", blackQuartz);
			
			Material Void = new Material("void_actadd_plustic", TextFormatting.BLACK);
			Void.addTrait(Unnamed.unnamed, HEAD);
			Void.addTrait(crude, HEAD);
			Void.addTrait(crude);
			ItemStack voidStack = new ItemStack(Item.REGISTRY.getObject(
					new ResourceLocation("ActuallyAdditions:itemCrystal")),
					1, 3);
			Void.addItem(voidStack, 1, Material.VALUE_Ingot);
			Void.setRepresentativeItem(voidStack);
			Void.setCraftable(true);
			proxy.setRenderInfo(Void, 0x222222);
			TinkerRegistry.addMaterialStats(Void, new HeadMaterialStats(480, 7, 4.4f, OBSIDIAN),
					new HandleMaterialStats(1, 0),
					new ExtraMaterialStats(140),
					new BowMaterialStats(1, 1.3f, 3.5f));
			materials.put("Void", Void);
		}
	}
	
	private static void initNatura() {
		if (Config.natura && Loader.isModLoaded("natura")) {
			boolean warned = false;
			
			Material darkwood = new Material("darkwood_plustic", TextFormatting.DARK_BLUE);
			darkwood.addTrait(DarkTraveler.darktraveler);
			darkwood.addTrait(ecological);
			darkwood.addItem(NaturaModuleStuff.darkwoodPlankStack, 1, Material.VALUE_Ingot);
			darkwood.addItem(NaturaModuleStuff.darkwoodLogStack, 1, 4*Material.VALUE_Ingot);
			try {
				darkwood.addItem(NaturaCommons.darkwood_stick, 1, Material.VALUE_Shard);
			} catch (NoSuchFieldError e) {
				warned = warnNatura(warned);
			}
			darkwood.setRepresentativeItem(NaturaModuleStuff.darkwoodPlankStack);
			darkwood.setCraftable(true);
			proxy.setRenderInfo(darkwood, 0x000044);
			TinkerRegistry.addMaterialStats(darkwood,
					new HeadMaterialStats(350, 5f, 3f, COBALT),
                    new HandleMaterialStats(1.3f, -5),
                    new ExtraMaterialStats(90),
                    new BowMaterialStats(1.2f, 1.3f, 3));
			materials.put("darkwood", darkwood);
		}
	}
	
	private static boolean warnNatura(boolean warned) {
		if (!warned) {
			log.warn("It is recommended that you have at least Natura 4.1.0.29 for integration (PlusTiC) with Tinkers Construct");
		}
		return true;
	}
	
	private static void initPsi() {
		if (Config.psi && Loader.isModLoaded("Psi")) {
			Material psimetal = new Material("psimetal", 0x6D9EFF);
			psimetal.addTrait(Psicological.psicological);
			psimetal.addItem("ingotPsi", 1, Material.VALUE_Ingot);
			psimetal.setCraftable(false).setCastable(true);
			Utils.setDispItem(psimetal, "ingotPsi");
			proxy.setRenderInfo(psimetal, 0x6D9EFF);
			
			FluidMolten psimetalFluid = Utils.fluidMetal("psimetal", 0x6D9EFF);
			psimetalFluid.setTemperature(696);
			Utils.initFluidMetal(psimetalFluid);
			psimetal.setFluid(psimetalFluid);
			
			TinkerRegistry.addMaterialStats(psimetal,
					new HeadMaterialStats(620, 7f, 5, OBSIDIAN),
					new HandleMaterialStats(1.3f, -10),
					new ExtraMaterialStats(30),
					new BowMaterialStats(1, 1.6f, 4));
			
			materials.put("psi", psimetal);
		}
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
	}

	private static void integrate(Map<String,Material> materials,
			Map<String,MaterialIntegration> materialIntegrations) {
		materials.forEach((k, v) -> {
			MaterialIntegration mi;
			if (v.getFluid() != null && v.getFluid() != TinkerFluids.emerald)
				mi = new MaterialIntegration(v, v.getFluid(), StringUtils.capitalize(k)).toolforge();
			else
				mi = new MaterialIntegration(v);
			mi.integrate();
			mi.integrateRecipes();
			materialIntegrations.put(k, mi);
		});
	}
}
