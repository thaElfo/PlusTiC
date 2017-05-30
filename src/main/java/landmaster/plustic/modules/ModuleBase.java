package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.tools.stats.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.init.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fluids.*;
import slimeknights.tconstruct.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

public class ModuleBase {

	public static void init() {
		if (Config.base) {
			Material tnt = new Material("tnt", TextFormatting.RED);
			tnt.addTrait(Explosive.explosive);
			tnt.addItem(Blocks.TNT, Material.VALUE_Ingot);
			tnt.setRepresentativeItem(Blocks.TNT);
			tnt.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(tnt, 0xFF4F4F);
			TinkerRegistry.addMaterialStats(tnt, new ArrowShaftMaterialStats(0.95f, 0));
			PlusTiC.materials.put("tnt", tnt);
			
			Material emerald = new Material("emerald_plustic", TextFormatting.GREEN);
			emerald.addTrait(Terrafirma.terrafirma.get(0));
			emerald.addTrait(Elemental.elemental, HEAD);
			emerald.addItem("gemEmerald", 1, Material.VALUE_Ingot);
			emerald.setRepresentativeItem(Items.EMERALD);
			emerald.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(emerald, 0x13DB52);
			emerald.setFluid(TinkerFluids.emerald);
			TinkerRegistry.addMaterialStats(emerald, new HeadMaterialStats(1222, 7, 7, COBALT),
					new HandleMaterialStats(1.1f, 0),
					new ExtraMaterialStats(70),
					new BowMaterialStats(1.1f, 1, 0.9f));
			PlusTiC.materials.put("emerald", emerald);
			
			if (TinkerIntegration.isIntegrated(TinkerFluids.aluminum)) {
				// alumite is back! (with some changes)
				Utils.ItemMatGroup alumiteGroup = Utils.registerMatGroup("alumite");
				
				Material alumite = new Material("alumite", TextFormatting.RED);
				alumite.addTrait(Global.global);
				alumite.addItem("ingotAlumite", 1, Material.VALUE_Ingot);
				alumite.setCraftable(false).setCastable(true);
				alumite.setRepresentativeItem(alumiteGroup.ingot);
				PlusTiC.proxy.setRenderInfo(alumite, 0xFFE0F1);
				
				FluidMolten alumiteFluid = Utils.fluidMetal("alumite", 0xFFE0F1);
				alumiteFluid.setTemperature(890);
				Utils.initFluidMetal(alumiteFluid);
				alumite.setFluid(alumiteFluid);
				TinkerRegistry.registerAlloy(new FluidStack(alumiteFluid, 3), new FluidStack(TinkerFluids.aluminum, 5),
						new FluidStack(TinkerFluids.iron, 2), new FluidStack(TinkerFluids.obsidian, 2));
				
				TinkerRegistry.addMaterialStats(alumite, new HeadMaterialStats(700, 6.8f, 5.5f, COBALT),
						new HandleMaterialStats(1.10f, 70), new ExtraMaterialStats(80),
						new BowMaterialStats(0.65f, 1.6f, 7f));
				
				PlusTiC.materials.put("alumite", alumite);
			}
			
			if (TinkerIntegration.isIntegrated(TinkerFluids.nickel)
					&& TinkerRegistry.getMaterial("nickel") == Material.UNKNOWN) {
				Material nickel = new Material("nickel", TextFormatting.YELLOW);
				nickel.addTrait(NickOfTime.nickOfTime, HEAD);
				nickel.addTrait(magnetic);
				nickel.addItem("ingotNickel", 1, Material.VALUE_Ingot);
				nickel.setCraftable(false).setCastable(true);
				Utils.setDispItem(nickel, "ingotNickel");
				PlusTiC.proxy.setRenderInfo(nickel, 0xFFF98E);
				
				nickel.setFluid(TinkerFluids.nickel);
				
				TinkerRegistry.addMaterialStats(nickel, new HeadMaterialStats(460, 6, 4.5f, OBSIDIAN),
						new HandleMaterialStats(1, -5), new ExtraMaterialStats(70), PlusTiC.justWhy,
						new FletchingMaterialStats(0.95f, 1.05f), new BatteryCellMaterialStats(75000));
				
				PlusTiC.materials.put("nickel", nickel);
			}
			
			if (TinkerIntegration.isIntegrated(TinkerFluids.nickel)
					&& TinkerRegistry.getMaterial("invar") == Material.UNKNOWN) {
				Utils.ItemMatGroup invarGroup = Utils.registerMatGroup("invar");
				
				Material invar = new Material("invar", 0xD6D6D6);
				invar.addTrait(DevilsStrength.devilsstrength);
				invar.addTrait(magnetic);
				invar.addItem("ingotInvar", 1, Material.VALUE_Ingot);
				invar.setCraftable(false).setCastable(true);
				invar.setRepresentativeItem(invarGroup.ingot);
				PlusTiC.proxy.setRenderInfo(invar, 0xD6D6D6);
				
				FluidMolten invarFluid = Utils.fluidMetal("invar", 0xD6D6D6);
				invarFluid.setTemperature(1000);
				Utils.initFluidMetal(invarFluid);
				invar.setFluid(invarFluid);
				TinkerRegistry.registerAlloy(new FluidStack(invarFluid, 3),
						new FluidStack(TinkerFluids.iron, 2),
						new FluidStack(TinkerFluids.nickel, 1));
				
				TinkerRegistry.addMaterialStats(invar,
						new HeadMaterialStats(600, 6, 5f, OBSIDIAN),
						new HandleMaterialStats(1.3f, 0),
						new ExtraMaterialStats(100),
						PlusTiC.justWhy,
						new FletchingMaterialStats(1, 1.15f));
				
				PlusTiC.materials.put("invar", invar);
			}
		}
	}
	
}
