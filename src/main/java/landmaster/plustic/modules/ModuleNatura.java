package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import com.progwml6.natura.shared.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.traits.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleNatura {

	public static void init() {
		if (Config.natura && Loader.isModLoaded("natura")) {
			boolean warned = false;
			
			Material darkwood = new Material("darkwood_plustic", TextFormatting.DARK_BLUE);
			darkwood.addTrait(DarkTraveler.darktraveler);
			darkwood.addTrait(ecological);
			darkwood.addItem(ModuleNaturaStuff.darkwoodPlankStack, 1, Material.VALUE_Ingot);
			darkwood.addItem(ModuleNaturaStuff.darkwoodLogStack, 1, 4*Material.VALUE_Ingot);
			try {
				darkwood.addItem(NaturaCommons.darkwood_stick, 1, Material.VALUE_Shard);
			} catch (NoSuchFieldError e) {
				warned = warnNatura(warned);
			}
			darkwood.setRepresentativeItem(ModuleNaturaStuff.darkwoodPlankStack);
			darkwood.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(darkwood, 0x000044);
			TinkerRegistry.addMaterialStats(darkwood,
					new HeadMaterialStats(350, 5f, 3f, COBALT),
	                new HandleMaterialStats(1.3f, -5),
	                new ExtraMaterialStats(90),
	                new BowMaterialStats(1.2f, 1.3f, 3));
			PlusTiC.materials.put("darkwood", darkwood);
		}
	}

	private static boolean warnNatura(boolean warned) {
		if (!warned) {
			PlusTiC.log.warn("It is recommended that you have at least Natura 4.1.0.29 for integration (PlusTiC) with Tinkers Construct");
		}
		return true;
	}
	
}
