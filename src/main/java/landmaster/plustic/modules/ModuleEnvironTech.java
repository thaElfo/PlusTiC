package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.traits.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleEnvironTech implements IModule {
	@GameRegistry.ObjectHolder("environmentaltech:mica")
	public static final Item micaItem = null;
	
	public void init() {
		if (Config.environTech && Loader.isModLoaded("environmentaltech")) {
			Material mica = new Material("mica", 0xBBBBBB);
			mica.addTrait(Slashing.slashing);
			mica.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(mica, 0xBBBBBB);
			TinkerRegistry.addMaterialStats(mica, new HeadMaterialStats(680, 5.5f, 6, OBSIDIAN),
					new HandleMaterialStats(0.9f, -5),
					new ExtraMaterialStats(90),
					PlusTiC.justWhy);
			PlusTiC.materials.put("mica", mica);
		}
	}
	
	public void init2() {
		final Material mica = PlusTiC.materials.get("mica");
		if (mica != null) {
			mica.addItem(micaItem, 1, Material.VALUE_Ingot);
			mica.setRepresentativeItem(micaItem);
		}
	}
}
