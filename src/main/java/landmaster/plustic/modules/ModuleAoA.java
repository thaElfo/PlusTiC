package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.util.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleAoA implements IModule {
	@Override
	public void init() {
		if (Config.aoa && Loader.isModLoaded("aoa3")) {
			Material limonite = new Material("limonite", 0xFFD400);
			limonite.addTrait(magnetic2, HEAD);
			limonite.addTrait(cheap);
			limonite.addItem("ingotLimonite", 1, Material.VALUE_Ingot);
			limonite.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(limonite, 0xFFD400);
			
			FluidMolten limoniteFluid = Utils.fluidMetal("limonite", 0xFFD400);
			limoniteFluid.setTemperature(747);
			Utils.initFluidMetal(limoniteFluid);
			limonite.setFluid(limoniteFluid);
			
			TinkerRegistry.addMaterialStats(limonite,
					new HeadMaterialStats(640, 6.5f, 4.5f, OBSIDIAN),
					new HandleMaterialStats(1, 70),
					new ExtraMaterialStats(-20),
					new BowMaterialStats(0.7f, 1.5f, 6f));
			
			PlusTiC.materials.put("limonite", limonite);
		}
	}
}
