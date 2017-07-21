package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleGalacticraft implements IModule {
	public void init() {
		if (Config.galacticraft && Loader.isModLoaded("galacticraftcore")) {
			if (Loader.isModLoaded("galacticraftplanets")) {
				Material desh = new Material("desh", 0x161616);
				desh.addTrait(alien);
				desh.addTrait(enderference);
				desh.addTrait(BrownMagic.brownmagic);
				desh.setCraftable(false).setCastable(true);
				desh.addItem("ingotDesh", 1, Material.VALUE_Ingot);
				PlusTiC.proxy.setRenderInfo(desh, 0x161616);
				
				FluidMolten deshFluid = Utils.fluidMetal("desh", 0x161616);
				deshFluid.setTemperature(821); // {821,823,827,829} are quadruplet primes
				Utils.initFluidMetal(deshFluid);
				desh.setFluid(deshFluid);
				
				TinkerRegistry.addMaterialStats(desh,
						new HeadMaterialStats(1729/* 1729 is a taxicab number */, (float)(Math.E*Math.PI), 8, COBALT),
						new HandleMaterialStats((float)(Math.sqrt(5)+1)/2/* the golden ratio */, 0),
						new ExtraMaterialStats(257),
						new BowMaterialStats(1/(float)Math.E, (float)Math.E, 12));
				
				PlusTiC.materials.put("desh", desh);
			}
		}
	}
}
