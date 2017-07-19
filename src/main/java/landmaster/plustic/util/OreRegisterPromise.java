package landmaster.plustic.util;

import java.util.*;
import java.util.concurrent.*;

import com.google.common.collect.*;

import landmaster.plustic.api.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.oredict.*;

/**
 * A little solution I rolled out for handling <em>extremely</em> annoying registry event mayhem…
 * @author Landmaster
 *
 */
@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class OreRegisterPromise extends CompletableFuture<ItemStack> {
	private static final Multimap<String, OreRegisterPromise> promises = MultimapBuilder.hashKeys().arrayListValues().build();
	
	private final String ore;
	
	public OreRegisterPromise(String ore) {
		this.ore = ore;
		
		this.whenComplete((stack, ex) -> promises.remove(this.ore, this)); // deregister promises when resolved
		
		final List<ItemStack> ores = OreDictionary.getOres(this.ore);
		if (ores.isEmpty()) {
			promises.put(this.ore, this);
		} else {
			this.complete(ores.get(0));
		}
	}
	
	/**
	 * This does the work of resolving the promises.
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onOreRegister(OreDictionary.OreRegisterEvent event) {
		new ArrayList<>(promises.get(event.getName())) // Have to wrap in an ArrayList to prevent comodification.
		.forEach(promise -> promise.complete(event.getOre()));
	}
}
