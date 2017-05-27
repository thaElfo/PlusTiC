package landmaster.plustic.modules;

import java.util.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.tools.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.tools.*;

public class ModuleTools {
	public static ToolKatana katana;
	
	private static final List<ToolCore> tools = new ArrayList<>();
	
	public static void init() {
		if (Config.katana) {
			katana = new ToolKatana();
			GameRegistry.register(katana);
			TinkerRegistry.registerToolForgeCrafting(katana);
			PlusTiC.proxy.registerToolModel(katana);
			
			tools.add(katana);
		}
		
		// for added PlusTiC tools
		for (IModifier modifier: TinkerRegistry.getAllModifiers()) {
			PlusTiC.proxy.registerModifierModel(modifier,
					new ResourceLocation(PlusTiC.MODID, "models/item/modifiers/"+modifier.getIdentifier()));
		}
	}
	
	public static List<ToolCore> getPlusTiCTools() {
		return Collections.unmodifiableList(tools);
	}
}
