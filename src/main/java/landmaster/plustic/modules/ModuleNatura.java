package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import java.util.Optional;
import java.util.concurrent.*;

import com.progwml6.natura.shared.*;

import landmaster.plustic.*;
import landmaster.plustic.api.*;
import landmaster.plustic.config.*;
import landmaster.plustic.traits.*;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class ModuleNatura implements IModule {
	private static final CompletableFuture<Object> integrationPromise = new CompletableFuture<>();

	public void init() {
		if (Config.natura && Loader.isModLoaded("natura")) {
			Material darkwood = new Material("darkwood_plustic", TextFormatting.DARK_BLUE);
			darkwood.addTrait(DarkTraveler.darktraveler);
			darkwood.addTrait(ecological);
			darkwood.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(darkwood, 0x000044);
			TinkerRegistry.addMaterialStats(darkwood,
					new HeadMaterialStats(350, 5f, 3f, COBALT),
	                new HandleMaterialStats(1.3f, -5),
	                new ExtraMaterialStats(90),
	                new BowMaterialStats(1.2f, 1.3f, 3));
			PlusTiC.materials.put("darkwood", darkwood);
			PlusTiC.materialIntegrationStages.put("darkwood", integrationPromise);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void itemRegEvent(RegistryEvent.Register<Item> event) {
		Optional.ofNullable(PlusTiC.materials.get("darkwood"))
		.ifPresent(darkwood -> {
			darkwood.addItem(ModuleNaturaStuff.darkwoodPlankStack(), 1, Material.VALUE_Ingot);
			darkwood.addItem(ModuleNaturaStuff.darkwoodLogStack(), 1, 4*Material.VALUE_Ingot);
			darkwood.addItem(NaturaCommons.darkwood_stick, 1, Material.VALUE_Shard);
			darkwood.setRepresentativeItem(ModuleNaturaStuff.darkwoodPlankStack());
		});
		integrationPromise.complete(null);
	}
}
