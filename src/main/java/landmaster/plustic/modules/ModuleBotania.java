package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

public class ModuleBotania {

	public static void init() {
		if (Config.botania && Loader.isModLoaded("Botania")) {
			Material terrasteel = new Material("terrasteel", TextFormatting.GREEN);
			terrasteel.addTrait(Mana.mana);
			terrasteel.addTrait(Terrafirma.terrafirma.get(0));
			terrasteel.addTrait(Mana.mana, HEAD);
			terrasteel.addTrait(Terrafirma.terrafirma.get(1), HEAD);
			terrasteel.addItem("ingotTerrasteel", 1, Material.VALUE_Ingot);
			terrasteel.setCraftable(false).setCastable(true);
			Utils.setDispItem(terrasteel, "botania", "manaResource", 4);
			PlusTiC.proxy.setRenderInfo(terrasteel, 0x00FF00);
			
			FluidMolten terrasteelFluid = Utils.fluidMetal("terrasteel", 0x00FF00);
			terrasteelFluid.setTemperature(760);
			Utils.initFluidMetal(terrasteelFluid);
			terrasteel.setFluid(terrasteelFluid);
			
			TinkerRegistry.addMaterialStats(terrasteel, new HeadMaterialStats(1562, 9, 6.5f, COBALT));
			TinkerRegistry.addMaterialStats(terrasteel, new HandleMaterialStats(1.4f, 10));
			TinkerRegistry.addMaterialStats(terrasteel, new ExtraMaterialStats(10));
			TinkerRegistry.addMaterialStats(terrasteel, new BowMaterialStats(0.55f, 2f, 11f));
			
			PlusTiC.materials.put("terrasteel", terrasteel);
			
			Material elementium = new Material("elementium", TextFormatting.LIGHT_PURPLE);
			elementium.addTrait(Mana.mana);
			elementium.addTrait(Mana.mana, HEAD);
			elementium.addTrait(Elemental.elemental, HEAD);
			elementium.addItem("ingotElvenElementium", 1, Material.VALUE_Ingot);
			elementium.setCraftable(false).setCastable(true);
			Utils.setDispItem(elementium, "botania", "manaResource", 7);
			PlusTiC.proxy.setRenderInfo(elementium, 0xF66AFD);
			
			FluidMolten elementiumFluid = Utils.fluidMetal("elementium", 0xF66AFD);
			elementiumFluid.setTemperature(800);
			Utils.initFluidMetal(elementiumFluid);
			elementium.setFluid(elementiumFluid);
			
			TinkerRegistry.addMaterialStats(elementium, new HeadMaterialStats(540, 7.00f, 6.00f, OBSIDIAN),
					new HandleMaterialStats(1.25f, 150), new ExtraMaterialStats(60));
			TinkerRegistry.addMaterialStats(elementium, new BowMaterialStats(0.8f, 1.5f, 7.5f));
			
			PlusTiC.materials.put("elvenElementium", elementium);
			
			Material manasteel = new Material("manasteel", TextFormatting.BLUE);
			manasteel.addTrait(Mana.mana);
			manasteel.addItem("ingotManasteel", 1, Material.VALUE_Ingot);
			manasteel.setCraftable(false).setCastable(true);
			Utils.setDispItem(manasteel, "botania", "manaResource");
			PlusTiC.proxy.setRenderInfo(manasteel, 0x54E5FF);
			
			FluidMolten manasteelFluid = Utils.fluidMetal("manasteel", 0x54E5FF);
			manasteelFluid.setTemperature(681);
			Utils.initFluidMetal(manasteelFluid);
			manasteel.setFluid(manasteelFluid);
			
			TinkerRegistry.addMaterialStats(manasteel, new HeadMaterialStats(540, 7.00f, 6.00f, OBSIDIAN),
					new HandleMaterialStats(1.25f, 150), new ExtraMaterialStats(60));
			TinkerRegistry.addMaterialStats(manasteel, new BowMaterialStats(1, 1.1f, 1));
			
			PlusTiC.materials.put("manasteel", manasteel);
			
			Material livingwood = new Material("livingwood_plustic", TextFormatting.DARK_GREEN);
			livingwood.addTrait(Botanical.botanical.get(1), HEAD);
			livingwood.addTrait(ecological, HEAD);
			livingwood.addTrait(Botanical.botanical.get(0));
			livingwood.addTrait(ecological);
			livingwood.addItem("livingwood", 1, Material.VALUE_Ingot);
			livingwood.setCraftable(true);
			Utils.setDispItem(livingwood, "livingwood");
			PlusTiC.proxy.setRenderInfo(livingwood, 0x560018);
			TinkerRegistry.addMaterialStats(livingwood, new HeadMaterialStats(50, 5.1f, 2.8f, IRON),
					new HandleMaterialStats(1.15f, 20), new ExtraMaterialStats(20),
					new BowMaterialStats(1.1f, 1.1f, 1.8f), new ArrowShaftMaterialStats(1f, 6));
			PlusTiC.materials.put("livingwood", livingwood);
			
			// MIRION ALLOY
			Utils.ItemMatGroup mirionGroup = Utils.registerMatGroup("mirion");
			
			Material mirion = new Material("mirion", TextFormatting.YELLOW);
			mirion.addTrait(Mirabile.mirabile, HEAD);
			mirion.addTrait(Mana.mana, HEAD);
			mirion.addTrait(Mana.mana);
			mirion.addItem("ingotMirion", 1, Material.VALUE_Ingot);
			mirion.setCraftable(false).setCastable(true);
			mirion.setRepresentativeItem(mirionGroup.ingot);
			PlusTiC.proxy.setRenderInfo(mirion, 0xDDFF00);
			
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
			
			PlusTiC.materials.put("mirion", mirion);
		}
	}
	
}
