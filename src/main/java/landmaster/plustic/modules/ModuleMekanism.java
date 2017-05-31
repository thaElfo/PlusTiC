package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.util.*;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.oredict.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

public class ModuleMekanism {

	public static void init() {
		if (Config.mekanism && Loader.isModLoaded("Mekanism")) {
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
			if (OreDictionary.getOres("ingotTin").size() == 0
					|| TinkerRegistry.getMelting(OreDictionary.getOres("ingotTin").get(0)) == null) {
				MaterialIntegration tinI = new MaterialIntegration(null, TinkerFluids.tin, "Tin");
				tinI.integrate();
				tinI.integrateRecipes();
				PlusTiC.materialIntegrations.put("tin", tinI);
			}
			
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
			
			TinkerRegistry.addMaterialStats(osmium, new HeadMaterialStats(500, 6, 5.8f, DIAMOND));
			TinkerRegistry.addMaterialStats(osmium, new HandleMaterialStats(1.2f, 45));
			TinkerRegistry.addMaterialStats(osmium, new ExtraMaterialStats(40));
			TinkerRegistry.addMaterialStats(osmium, new BowMaterialStats(0.65f, 1.3f, 5.7f));
			
			PlusTiC.materials.put("osmium", osmium);
			
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
			
			TinkerRegistry.addMaterialStats(refinedObsidian, new HeadMaterialStats(2500, 7, 11, COBALT));
			TinkerRegistry.addMaterialStats(refinedObsidian, new HandleMaterialStats(1.5f, -100));
			TinkerRegistry.addMaterialStats(refinedObsidian, new ExtraMaterialStats(160));
			TinkerRegistry.addMaterialStats(refinedObsidian, PlusTiC.justWhy);
			
			PlusTiC.materials.put("refinedObsidian", refinedObsidian);
		}
	}
	
}
