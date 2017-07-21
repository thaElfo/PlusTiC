package landmaster.plustic.modules;

import landmaster.plustic.config.*;
import landmaster.plustic.modifiers.*;
import landmaster.plustic.util.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;

public class ModuleModifiers implements IModule {
	public void init() {
		if (Config.enderIO && Loader.isModLoaded("enderio")) {
			TinkerRegistry.registerModifier(ModEndlectric.endlectric);
			Utils.addModifierItem(ModEndlectric.endlectric, "enderio", "itemBasicCapacitor", 2);
		}
	}
}
