package landmaster.plustic.config;

import java.util.*;
import com.google.common.collect.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.event.*;

public class Config extends Configuration {
	public static boolean base;
	public static boolean bop;
	public static boolean projectRed;
	public static boolean mekanism;
	public static boolean botania;
	public static boolean advancedRocketry;
	public static boolean armorPlus;
	public static boolean enderIO;
	public static boolean thermalFoundation;
	public static boolean draconicEvolution;
	public static boolean actuallyAdditions;
	public static ArrayListMultimap<Item, ItemStack> endlectricBlacklist = ArrayListMultimap.create();
	
	public Config(FMLPreInitializationEvent event) {
		super(event.getSuggestedConfigurationFile());
	}
	
	public void sync() {
		base = getBoolean("Enable vanilla TiC addons", "modules", true, "Add features to vanilla Tinkers Construct");
		bop = getBoolean("Enable BoP integration", "modules", true, "Integrate with Biomes o' Plenty");
		projectRed = getBoolean("Enable Project Red integration", "modules", true, "Integrate with Project Red-Exploration");
		mekanism = getBoolean("Enable Mekanism integration", "modules", true, "Integrate with Mekanism");
		botania = getBoolean("Enable Botania integration", "modules", true, "Integrate with Botania");
		advancedRocketry = getBoolean("Enable Advanced Rocketry integration", "modules", true, "Integrate with Advanced Rocketry (actually LibVulpes)");
		armorPlus = getBoolean("Enable ArmorPlus integration", "modules", true, "Integrate with ArmorPlus");
		enderIO = getBoolean("Enable EnderIO integration", "modules", true, "Integrate with EnderIO");
		thermalFoundation = getBoolean("Enable Thermal Foundation integration", "modules", true, "Integrate with Thermal Foundation");
		draconicEvolution = getBoolean("Enable Draconic Evolution integration", "modules", true, "Integrate with Draconic Evolution");
		actuallyAdditions = getBoolean("Enable Actually Additions support", "modules", true, "Integrate with Actually Additions");
		String[] arr = getStringList("Items that Endlectric will not drain from", "tweaks", new String[] {}, "Enter in the format \"modid:name;meta\" (leave meta blank to match any meta)");
		int meta = -1;
		for (int i=0; i<arr.length; ++i) {
			String[] loc_meta = arr[i].split(";");
			if (loc_meta.length > 1) {
				try {
					meta = Integer.valueOf(loc_meta[1]);
				} catch (NumberFormatException e) {}
			}
			Item it = Item.REGISTRY.getObject(new ResourceLocation(loc_meta[0]));
			if (it != null) {
				endlectricBlacklist.put(it, (meta>=0 ? new ItemStack(it, 1, meta) : null));
			}
		}
		if (hasChanged()) save();
	}
	
	public static boolean isInEndlectricBlacklist(ItemStack is) {
		if (is != null && endlectricBlacklist.containsKey(is.getItem())) {
			List<ItemStack> lst = endlectricBlacklist.get(is.getItem());
			for (ItemStack is1: lst) {
				if (is1 == null || is.getMetadata() == is1.getMetadata()) return true;
			}
		}
		return false;
	}
}
