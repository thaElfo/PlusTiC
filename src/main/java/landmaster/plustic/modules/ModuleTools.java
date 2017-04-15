package landmaster.plustic.modules;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.tools.*;
import net.minecraftforge.fml.common.registry.*;
import slimeknights.tconstruct.library.*;

public class ModuleTools {

	public static void init() {
		if (Config.katana) {
			PlusTiC.katana = new ToolKatana();
			GameRegistry.register(PlusTiC.katana);
			TinkerRegistry.registerToolForgeCrafting(PlusTiC.katana);
			PlusTiC.proxy.registerToolModel(PlusTiC.katana);
		}
	}
	
}
