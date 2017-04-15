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

public class ModuleAdvRocketry {

	public static void init() {
		if (Config.advancedRocketry && Loader.isModLoaded("libVulpes")) {
			Material iridium = new Material("iridium", TextFormatting.GRAY);
			iridium.addTrait(dense);
			iridium.addTrait(alien, HEAD);
			iridium.addItem("ingotIridium", 1, Material.VALUE_Ingot);
			iridium.setCraftable(false).setCastable(true);
			Utils.setDispItem(iridium, "libvulpes", "productingot", 10);
			PlusTiC.proxy.setRenderInfo(iridium, 0xE5E5E5);
			
			FluidMolten iridiumFluid = Utils.fluidMetal("iridium", 0xE5E5E5);
			iridiumFluid.setTemperature(810);
			Utils.initFluidMetal(iridiumFluid);
			iridium.setFluid(iridiumFluid);
			
			TinkerRegistry.addMaterialStats(iridium, new HeadMaterialStats(520, 6, 5.8f, DIAMOND));
			TinkerRegistry.addMaterialStats(iridium, new HandleMaterialStats(1.15f, -20));
			TinkerRegistry.addMaterialStats(iridium, new ExtraMaterialStats(60));
			TinkerRegistry.addMaterialStats(iridium, PlusTiC.justWhy);
			
			PlusTiC.materials.put("iridium", iridium);
			
			Material titanium = new Material("titanium", TextFormatting.WHITE);
			titanium.addTrait(Light.light);
			titanium.addTrait(Anticorrosion.anticorrosion, HEAD);
			titanium.addItem("ingotTitanium", 1, Material.VALUE_Ingot);
			titanium.setCraftable(false).setCastable(true);
			Utils.setDispItem(titanium, "libvulpes", "productingot", 7);
			PlusTiC.proxy.setRenderInfo(titanium, 0xDCE1EA);
			
			FluidMolten titaniumFluid = Utils.fluidMetal("titanium", 0xDCE1EA);
			titaniumFluid.setTemperature(790);
			Utils.initFluidMetal(titaniumFluid);
			titanium.setFluid(titaniumFluid);
			
			TinkerRegistry.addMaterialStats(titanium, new HeadMaterialStats(560, 6, 6, OBSIDIAN));
			TinkerRegistry.addMaterialStats(titanium, new HandleMaterialStats(1.4f, 0));
			TinkerRegistry.addMaterialStats(titanium, new ExtraMaterialStats(40));
			TinkerRegistry.addMaterialStats(titanium, new BowMaterialStats(1.15f, 1.3f, 6.6f));
			TinkerRegistry.addMaterialStats(titanium, new FletchingMaterialStats(1.0f, 1.3f));
			
			PlusTiC.materials.put("titanium", titanium);
			
			if (Config.mekanism && Loader.isModLoaded("Mekanism")) {
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
						new FluidStack(PlusTiC.materials.get("osmium").getFluid(), 1), new FluidStack(iridiumFluid, 1));
				
				TinkerRegistry.addMaterialStats(osmiridium, new HeadMaterialStats(1300, 6.8f, 8, COBALT));
				TinkerRegistry.addMaterialStats(osmiridium, new HandleMaterialStats(1.5f, 30));
				TinkerRegistry.addMaterialStats(osmiridium, new ExtraMaterialStats(80));
				TinkerRegistry.addMaterialStats(osmiridium, new BowMaterialStats(0.38f, 2.05f, 10));
				
				PlusTiC.materials.put("osmiridium", osmiridium);
			}
		}
	}
	
}
