package landmaster.plustic.modules;

import java.util.*;

import c4.conarm.lib.materials.*;
import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.traits.armor.*;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.library.*;

public class ModuleConArm implements IModule {
	@Override
	public void init() {
		if (Config.constructsArmory && Loader.isModLoaded("conarm")) {
			Optional.ofNullable(PlusTiC.materials.get("emerald")).ifPresent(mat -> {
				ArmorMaterials.addArmorTrait(mat, GoodFridayAgreement.goodfridayagreement);
				ArmorMaterials.addArmorTrait(mat, Terrafirma.terrafirma.get(0));
				TinkerRegistry.addMaterialStats(mat,
						new CoreMaterialStats(18, 20),
						new PlatesMaterialStats(1, 9, 2.2f),
						new TrimMaterialStats(13.5f));
			});
		}
	}
}
