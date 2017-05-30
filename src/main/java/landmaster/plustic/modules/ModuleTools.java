package landmaster.plustic.modules;

import java.util.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.tools.*;
import landmaster.plustic.tools.stats.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.tools.*;
import slimeknights.tconstruct.tools.*;

public class ModuleTools {
	public static ToolKatana katana;
	public static ToolLaserGun laserGun;
	
	public static ToolPart pipe_piece;
	public static ToolPart laser_medium;
	public static ToolPart battery_cell;
	
	private static final List<ToolCore> tools = new ArrayList<>();
	
	public static void init() {
		pipe_piece = new ToolPart(Material.VALUE_Ingot * 4);
		pipe_piece.setUnlocalizedName("pipe_piece").setRegistryName("pipe_piece");
		GameRegistry.register(pipe_piece);
		PlusTiC.proxy.registerToolPartModel(pipe_piece);
		
		laser_medium = new ToolPart(Material.VALUE_Ingot * 3);
		laser_medium.setUnlocalizedName("laser_medium").setRegistryName("laser_medium");
		GameRegistry.register(laser_medium);
		PlusTiC.proxy.registerToolPartModel(laser_medium);
		
		battery_cell = new ToolPart(Material.VALUE_Ingot * 3);
		battery_cell.setUnlocalizedName("battery_cell").setRegistryName("battery_cell");
		GameRegistry.register(battery_cell);
		PlusTiC.proxy.registerToolPartModel(battery_cell);
		
		if (Config.laserGun) {
			laserGun = new ToolLaserGun();
			GameRegistry.register(laserGun);
			TinkerRegistry.registerToolForgeCrafting(laserGun);
			PlusTiC.proxy.registerToolModel(laserGun);
			
			tools.add(laserGun);
			
			// Vanilla Tinkers Laser material stats
			TinkerRegistry.addMaterialStats(TinkerMaterials.prismarine, new LaserMediumMaterialStats(2.5f, 20));
			TinkerRegistry.addMaterialStats(TinkerMaterials.copper, new BatteryCellMaterialStats(55000));
			TinkerRegistry.addMaterialStats(TinkerMaterials.silver, new BatteryCellMaterialStats(75000));
			TinkerRegistry.addMaterialStats(TinkerMaterials.manyullyn, new BatteryCellMaterialStats(120000));
		}
		
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
