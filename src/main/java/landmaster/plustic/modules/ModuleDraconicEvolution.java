package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;

import com.brandon3055.draconicevolution.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.traits.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleDraconicEvolution {

	public static void init() {
		if (Config.draconicEvolution && Loader.isModLoaded("draconicevolution")) {
			Material wyvern = new Material("wyvern_plustic", TextFormatting.DARK_PURPLE);
			wyvern.addTrait(BrownMagic.brownmagic, HEAD);
			wyvern.addTrait(BlindBandit.blindbandit, HEAD);
			wyvern.addTrait(Portly.portly);
			wyvern.addItem(DEFeatures.wyvernCore, 1, Material.VALUE_Ingot);
			wyvern.setCraftable(true);
			wyvern.setRepresentativeItem(DEFeatures.wyvernCore);
			PlusTiC.proxy.setRenderInfo(wyvern, 0x7F00FF);
			TinkerRegistry.addMaterialStats(wyvern, new HeadMaterialStats(2000, 8, 15, 8));
			TinkerRegistry.addMaterialStats(wyvern, new HandleMaterialStats(1.6f, 130));
			TinkerRegistry.addMaterialStats(wyvern, new ExtraMaterialStats(240));
			TinkerRegistry.addMaterialStats(wyvern, new BowMaterialStats(1.6f, 2, 11));
			PlusTiC.materials.put("wyvern_core", wyvern);
			
			Material awakened = new Material("awakened_plustic", TextFormatting.GOLD);
			awakened.addTrait(RudeAwakening.rudeawakening, HEAD);
			awakened.addTrait(BrownMagic.brownmagic, HEAD);
			awakened.addTrait(BlindBandit.blindbandit);
			awakened.addTrait(Apocalypse.apocalypse);
			awakened.addTrait(Global.global);
			awakened.addItem(DEFeatures.awakenedCore, 1, Material.VALUE_Ingot);
			awakened.setCraftable(true);
			awakened.setRepresentativeItem(DEFeatures.awakenedCore);
			PlusTiC.proxy.setRenderInfo(awakened, 0xFFB200);
			TinkerRegistry.addMaterialStats(awakened, new HeadMaterialStats(5000, 9, 35, 10));
			TinkerRegistry.addMaterialStats(awakened, new HandleMaterialStats(1.8f, 500));
			TinkerRegistry.addMaterialStats(awakened, new ExtraMaterialStats(500));
			TinkerRegistry.addMaterialStats(awakened, new BowMaterialStats(1.9f, 2.8f, 20));
			PlusTiC.materials.put("awakened_core", awakened);
		}
	}
	
}
