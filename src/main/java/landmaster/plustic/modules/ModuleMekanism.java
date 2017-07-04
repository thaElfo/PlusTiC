package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.tools.stats.*;
import landmaster.plustic.traits.Anticorrosion;
import landmaster.plustic.traits.DevilsStrength;
import landmaster.plustic.util.*;
import mcjty.lib.tools.*;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.oredict.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

public class ModuleMekanism {

	public static void init() {
		if (Config.mekanism && (Loader.isModLoaded("Mekanism") || Loader.isModLoaded("mekanism"))) {
			// ugly workaround for dusts not melting
			Item tinDust = new Item().setUnlocalizedName("tindust").setRegistryName("tindust");
			tinDust.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(tinDust);
			OreDictionary.registerOre("dustTin", tinDust);
			PlusTiC.proxy.registerItemRenderer(tinDust, 0, "tindust");
			
			Item osmiumDust = new Item().setUnlocalizedName("osmiumdust").setRegistryName("osmiumdust");
			osmiumDust.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(osmiumDust);
			OreDictionary.registerOre("dustOsmium", osmiumDust);
			PlusTiC.proxy.registerItemRenderer(osmiumDust, 0, "osmiumdust");
			
			Item steelDust = new Item().setUnlocalizedName("steeldust").setRegistryName("steeldust");
			steelDust.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(steelDust);
			OreDictionary.registerOre("dustSteel", steelDust);
			PlusTiC.proxy.registerItemRenderer(steelDust, 0, "steeldust");
			
			
			Item bronzeNugget = new Item().setUnlocalizedName("bronzenugget").setRegistryName("bronzenugget");
			bronzeNugget.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(bronzeNugget);
			OreDictionary.registerOre("nuggetBronze", bronzeNugget);
			PlusTiC.proxy.registerItemRenderer(bronzeNugget, 0, "bronzenugget");
			
			Item bronzeIngot = new Item().setUnlocalizedName("bronzeingot").setRegistryName("bronzeingot");
			bronzeIngot.setCreativeTab(TinkerRegistry.tabGeneral);
			GameRegistry.register(bronzeIngot);
			OreDictionary.registerOre("ingotBronze", bronzeIngot);
			PlusTiC.proxy.registerItemRenderer(bronzeIngot, 0, "bronzeingot");
			
			
			// ugly workaround for molten tin not registering
			if (ItemStackTools.getOres("ingotTin").size() == 0
					|| TinkerRegistry.getMelting(ItemStackTools.getOres("ingotTin").get(0)) == null) {
				MaterialIntegration tinI = new MaterialIntegration(null, TinkerFluids.tin, "Tin");
				tinI.integrate();
				tinI.integrateRecipes();
				PlusTiC.materialIntegrations.put("tin", tinI);
			}
			
			if (TinkerRegistry.getMaterial("osmium") == Material.UNKNOWN) {
				Material osmium = new Material("osmium", TextFormatting.BLUE);
				osmium.addTrait(dense);
				osmium.addTrait(established);
				osmium.addItem("ingotOsmium", 1, Material.VALUE_Ingot);
				osmium.setCraftable(false).setCastable(true);
				PlusTiC.proxy.setRenderInfo(osmium, 0xBFD0FF);
				
				FluidMolten osmiumFluid = Utils.fluidMetal("osmium", 0xBFD0FF);
				osmiumFluid.setTemperature(820);
				Utils.initFluidMetal(osmiumFluid);
				osmium.setFluid(osmiumFluid);
				
				TinkerRegistry.addMaterialStats(osmium,
						new HeadMaterialStats(500, 6, 5.8f, DIAMOND),
						new HandleMaterialStats(1.2f, 45),
						new ExtraMaterialStats(40),
						new BowMaterialStats(0.65f, 1.3f, 5.7f),
						new BatteryCellMaterialStats(80000));
				
				PlusTiC.materials.put("osmium", osmium);
			}
			
			Material refinedObsidian = new Material("refinedObsidian", TextFormatting.LIGHT_PURPLE);
			refinedObsidian.addTrait(dense);
			refinedObsidian.addTrait(duritos);
			refinedObsidian.addItem("ingotRefinedObsidian", 1, Material.VALUE_Ingot);
			refinedObsidian.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(refinedObsidian, 0x5D00FF);
			
			FluidMolten refinedObsidianFluid = Utils.fluidMetal("refinedObsidian", 0x5D00FF);
			refinedObsidianFluid.setTemperature(860);
			Utils.initFluidMetal(refinedObsidianFluid);
			refinedObsidian.setFluid(refinedObsidianFluid);
			
			TinkerRegistry.addMaterialStats(refinedObsidian,
					new HeadMaterialStats(2500, 7, 11, COBALT),
					new HandleMaterialStats(1.5f, -100),
					new ExtraMaterialStats(160),
					PlusTiC.justWhy,
					new LaserMediumMaterialStats(6.0f, 45));
			
			PlusTiC.materials.put("refinedObsidian", refinedObsidian);
			
			if (PlusTiC.materials.containsKey("iridium")) {
				// osmiridium
				Utils.ItemMatGroup osmiridiumGroup = Utils.registerMatGroup("osmiridium");
				
				Material osmiridium = new Material("osmiridium", TextFormatting.LIGHT_PURPLE);
				osmiridium.addTrait(DevilsStrength.devilsstrength);
				osmiridium.addTrait(Anticorrosion.anticorrosion, HEAD);
				osmiridium.addItem("ingotOsmiridium", 1, Material.VALUE_Ingot);
				osmiridium.setCraftable(false).setCastable(true);
				osmiridium.setRepresentativeItem(osmiridiumGroup.ingot);
				PlusTiC.proxy.setRenderInfo(osmiridium, 0x666DFF);
				
				FluidMolten osmiridiumFluid = Utils.fluidMetal("osmiridium", 0x666DFF);
				osmiridiumFluid.setTemperature(840);
				Utils.initFluidMetal(osmiridiumFluid);
				osmiridium.setFluid(osmiridiumFluid);
				TinkerRegistry.registerAlloy(new FluidStack(osmiridiumFluid, 2),
						new FluidStack(PlusTiC.materials.get("osmium").getFluid(), 1), new FluidStack(PlusTiC.materials.get("iridium").getFluid(), 1));
				
				TinkerRegistry.addMaterialStats(osmiridium, new HeadMaterialStats(1300, 6.8f, 8, COBALT));
				TinkerRegistry.addMaterialStats(osmiridium, new HandleMaterialStats(1.5f, 30));
				TinkerRegistry.addMaterialStats(osmiridium, new ExtraMaterialStats(80));
				TinkerRegistry.addMaterialStats(osmiridium, new BowMaterialStats(0.38f, 2.05f, 10));
				
				PlusTiC.materials.put("osmiridium", osmiridium);
			}
		}
	}
	
}
