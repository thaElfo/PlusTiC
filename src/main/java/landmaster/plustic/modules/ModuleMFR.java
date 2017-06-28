package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.*;
import powercrystals.minefactoryreloaded.setup.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleMFR {
	public static void init() {
		if (Config.mfr && Loader.isModLoaded("minefactoryreloaded")) {
			Material pink_slime = new Material("pink_slime", 0xFF84AD);
			pink_slime.addTrait(ModuleMFRStuff.slimey_pink);
			pink_slime.addItem(new ItemStack(MFRThings.pinkSlimeItem, 1, 1), 1, Material.VALUE_Ingot);
			pink_slime.setCraftable(true);
			pink_slime.setRepresentativeItem(new ItemStack(MFRThings.pinkSlimeItem, 1, 1));
			PlusTiC.proxy.setRenderInfo(pink_slime, 0xFF84AD);
			
			TinkerRegistry.addMaterialStats(pink_slime,
					new HeadMaterialStats(1800, 3.77f, 1.80f, STONE),
					new HandleMaterialStats(2.7f, -729),
					new ExtraMaterialStats(243),
					new BowMaterialStats(1.2f, 0.8f, 0));
			
			PlusTiC.materials.put("pink_slime", pink_slime);
		}
	}
}
