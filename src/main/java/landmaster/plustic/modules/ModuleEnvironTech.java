package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

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
			
			Material litherite = new Material("litherite", 0x078E51);
			litherite.addTrait(stonebound, HEAD);
			litherite.addTrait(crude2);
			litherite.addTrait(petramor);
			litherite.addItem("crystalLitherite", 1, Material.VALUE_Ingot);
			litherite.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(mica, 0x078E51);
			TinkerRegistry.addMaterialStats(litherite, new HeadMaterialStats(700, 7.7f, 6, OBSIDIAN),
					new HandleMaterialStats(0.9f, -5),
					new ExtraMaterialStats(111),
					PlusTiC.justWhy);
			PlusTiC.materials.put("litherite", litherite);
			PlusTiC.materialOreDicts.put("litherite", "crystalLitherite");
			
			Material erodium = new Material("erodium", 0xBB56FF);
			erodium.addTrait(Vindictive.vindictive);
			erodium.addTrait(NaturesWrath.natureswrath);
			erodium.addItem("crystalErodium", 1, Material.VALUE_Ingot);
			erodium.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(erodium, 0xBB56FF);
			TinkerRegistry.addMaterialStats(erodium, new HeadMaterialStats(1000, 9f, 7.5f, OBSIDIAN),
					new HandleMaterialStats(0.9f, -5),
					new ExtraMaterialStats(140),
					PlusTiC.justWhy);
			PlusTiC.materials.put("erodium", erodium);
			PlusTiC.materialOreDicts.put("erodium", "crystalErodium");
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
