package landmaster.plustic.config;

import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.event.*;

public class Config extends Configuration {
	public static boolean base;
	public static boolean bop;
	public static boolean mekanism;
	public static boolean botania;
	public static boolean advancedRocketry;
	public static boolean armorPlus;
	public static boolean enderIO;
	
	public Config(FMLPreInitializationEvent event) {
		super(event.getSuggestedConfigurationFile());
	}
	
	public void sync() {
		base = getBoolean("Enable vanilla TiC addons", "modules", true, "Add features to vanilla Tinkers Construct");
		bop = getBoolean("Enable BoP integration", "modules", true, "Integrate with Biomes o' Plenty");
		mekanism = getBoolean("Enable Mekanism integration", "modules", true, "Integrate with Mekanism");
		botania = getBoolean("Enable Botania integration", "modules", true, "Integrate with Botania");
		advancedRocketry = getBoolean("Enable Advanced Rocketry integration", "modules", true, "Integrate with Advanced Rocketry");
		armorPlus = getBoolean("Enable ArmorPlus integration", "modules", true, "Integrate with ArmorPlus");
		enderIO = getBoolean("Enable EnderIO integration", "modules", true, "Integrate with EnderIO");
		if (hasChanged()) save();
	}
}
