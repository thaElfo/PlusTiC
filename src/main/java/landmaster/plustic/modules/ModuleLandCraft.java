package landmaster.plustic.modules;

import landmaster.plustic.config.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleLandCraft {
	public static void init() {
		if (Config.landCraft && Loader.isModLoaded("landcraft")) {
			Material kelline = new Material("kelline", TextFormatting.WHITE);
			
		}
	}
}
