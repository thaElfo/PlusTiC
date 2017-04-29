package landmaster.plustic.config;

import java.util.*;

import org.apache.commons.lang3.*;

import com.google.common.collect.*;

import landmaster.plustic.traits.*;
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
	public static boolean substratum;
	public static boolean natura;
	public static boolean psi;
	public static boolean avaritia;
	
	public static boolean katana;
	
	public static float katana_combo_multiplier;
	
	public static List<Integer> botan_amount;
	
	private static final ArrayListMultimap<Item, ItemStack> endlectricBlacklist = ArrayListMultimap.create();
	
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
		substratum = getBoolean("Enable Substratum support", "modules", true, "Integrate with Substratum");
		natura = getBoolean("Enable Natura support", "modules", true, "Integrate with Natura");
		psi = getBoolean("Enable Psi support", "modules", true, "Integrate with Psi");
		avaritia = getBoolean("Enable Avaritia support", "modules", true, "Integrate with Avaritia");
		
		katana = getBoolean("Enable Katana", "tools", true, "Enable Katana");
		katana_combo_multiplier = getFloat("Katana combo multiplier", "tools", 1.25f, 0, Float.MAX_VALUE, "Multiply combo value by this to calculate bonus damage");
		
		String[] arr_endlectric = getStringList("Items that Endlectric will not drain from", "tweaks", new String[] {}, "Enter in the format \"modid:name;meta\" (leave meta blank to match any meta)");
		
		Property botan_amount_prop = this.get("tweaks", "Modifier values for Botanical", new int[0]);
		botan_amount_prop.setLanguageKey("Modifiers added for Botanical modifier");
		botan_amount_prop.setComment("Enter in order of level (defaults will be extrapolated if some left blank)");
		botan_amount_prop.setMinValue(0);
		botan_amount = new ArrayList<>(Arrays.asList(
				ArrayUtils.toObject(botan_amount_prop.getIntList())));
		if (botan_amount.size() <= 0) botan_amount.add(1);
		while (botan_amount.size() < Botanical.MAX_LEVELS) {
			botan_amount.add(botan_amount.get(botan_amount.size()-1)<<1);
		}
		botan_amount = Collections.unmodifiableList(botan_amount);
		
		
		int meta = -1;
		for (int i=0; i<arr_endlectric.length; ++i) {
			String[] loc_meta = arr_endlectric[i].split(";");
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
