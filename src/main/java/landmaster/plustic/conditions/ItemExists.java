package landmaster.plustic.conditions;

import java.util.function.*;

import com.google.gson.*;

import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.common.crafting.*;

public class ItemExists implements IConditionFactory {
	
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		ResourceLocation rl = new ResourceLocation(JsonUtils.getString(json, "item"));
		boolean invert = JsonUtils.getBoolean(json, "invert", false);
		return () -> Item.REGISTRY.containsKey(rl) != invert;
	}
	
}
