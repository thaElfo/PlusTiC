package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleAoA implements IModule {
	@Override
	public void init() {
		if (Config.aoa && Loader.isModLoaded("aoa3")) {
			Material limonite = new Material("limonite", 0xFFD400);
			limonite.addTrait(magnetic2, HEAD);
			limonite.addTrait(cheap);
			limonite.addItem("ingotLimonite", 1, Material.VALUE_Ingot);
			limonite.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(limonite, 0xFFD400);
			
			FluidMolten limoniteFluid = Utils.fluidMetal("limonite", 0xFFD400);
			limoniteFluid.setTemperature(747);
			Utils.initFluidMetal(limoniteFluid);
			limonite.setFluid(limoniteFluid);
			
			TinkerRegistry.addMaterialStats(limonite,
					new HeadMaterialStats(640, 6.5f, 4.5f, OBSIDIAN),
					new HandleMaterialStats(1, 70),
					new ExtraMaterialStats(-20),
					new BowMaterialStats(0.7f, 1.5f, 6f));
			
			PlusTiC.materials.put("limonite", limonite);
			
			Material rosite = new Material("rosite", 0xEF2647);
			rosite.addTrait(Autorepair.autorepair);
			rosite.addItem("ingotRosite", 1, Material.VALUE_Ingot);
			rosite.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(rosite, 0xEF2647);
			
			FluidMolten rositeFluid = Utils.fluidMetal("rosite", 0xEF2647);
			rositeFluid.setTemperature(888);
			Utils.initFluidMetal(rositeFluid);
			rosite.setFluid(rositeFluid);
			
			TinkerRegistry.addMaterialStats(rosite,
					new HeadMaterialStats(2000, 9, 6, COBALT),
					new HandleMaterialStats(1.5f, -600),
					new ExtraMaterialStats(200),
					new BowMaterialStats(1, 1, 6f));
			
			PlusTiC.materials.put("rosite", rosite);
			
			Material crystallite = new Material("crystallite", 0xFFC14D);
			crystallite.addTrait(Global.global);
			crystallite.addTrait(BrownMagic.brownmagic);
			crystallite.addItem("gemCrystallite", 1, Material.VALUE_Ingot);
			new OreRegisterPromise("gemCrystallite").thenAccept(crystallite::setRepresentativeItem);
			crystallite.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(crystallite, 0xFFC14D);
			
			TinkerRegistry.addMaterialStats(crystallite,
					new HeadMaterialStats(1000, 7, 10, 5),
					new HandleMaterialStats(0.8f, 20),
					new ExtraMaterialStats(70),
					PlusTiC.justWhy);
			PlusTiC.materials.put("crystallite", crystallite);
			
			Material emberstone = new Material("emberstone", 0x450009);
			emberstone.addTrait(autosmelt, HEAD);
			emberstone.addTrait(hellish, HEAD);
			emberstone.addTrait(autosmelt);
			emberstone.addItem("ingotEmberstone", 1, Material.VALUE_Ingot);
			emberstone.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(emberstone, 0x450009);
			
			FluidMolten emberstoneFluid = Utils.fluidMetal("emberstone", 0x450009);
			emberstoneFluid.setTemperature(1000);
			Utils.initFluidMetal(emberstoneFluid);
			emberstone.setFluid(emberstoneFluid);
			
			TinkerRegistry.addMaterialStats(emberstone,
					new HeadMaterialStats(1100, 9, 9.5f, COBALT),
					new HandleMaterialStats(1, 0),
					new ExtraMaterialStats(120),
					new BowMaterialStats(0.4f, 2, 10));
			PlusTiC.materials.put("emberstone", emberstone);
			
			Material skeletal = new Material("skeletal", 0xE8E3C5);
			skeletal.addTrait(sharp, HEAD);
			skeletal.addTrait(fractured);
			skeletal.addItem("ingotSkeletal", 1, Material.VALUE_Ingot);
			skeletal.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(skeletal, 0xE8E3C5);
			
			FluidMolten skeletalFluid = Utils.fluidMetal("skeletal", 0xE8E3C5);
			skeletalFluid.setTemperature(888);
			Utils.initFluidMetal(skeletalFluid);
			skeletal.setFluid(skeletalFluid);
			
			TinkerRegistry.addMaterialStats(skeletal,
					new HeadMaterialStats(1500, 12, 12, 4),
					new HandleMaterialStats(1, 200),
					new ExtraMaterialStats(200),
					new BowMaterialStats(0.3f, 2, 13));
			PlusTiC.materials.put("skeletal", skeletal);
			
			Material mystite = new Material("mystite", 0x29ffc2);
			mystite.addTrait(SoulPower.soulpower);
			mystite.addItem("ingotMystite", 1, Material.VALUE_Ingot);
			mystite.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(mystite, 0x29ffc2);
			
			FluidMolten mystiteFluid = Utils.fluidMetal("mystite", 0x29ffc2);
			mystiteFluid.setTemperature(361);
			Utils.initFluidMetal(mystiteFluid);
			mystite.setFluid(mystiteFluid);
			
			TinkerRegistry.addMaterialStats(mystite,
					new HeadMaterialStats(2000, 14, 4, 6),
					new HandleMaterialStats(0.7f, -50),
					new ExtraMaterialStats(0),
					new BowMaterialStats(1, 1, 5));
			PlusTiC.materials.put("mystite", mystite);
			
			Material ghostlyStone = new Material("ghostlystone", 0xe8fcff);
			ghostlyStone.addTrait(SoulCharge.soulcharge);
			ghostlyStone.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(ghostlyStone, 0xe8fcff);
			TinkerRegistry.addMaterialStats(ghostlyStone,
					new HeadMaterialStats(50, 4, 2, STONE),
					new HandleMaterialStats(1.5f, 0),
					new ExtraMaterialStats(400),
					PlusTiC.justWhy);
			PlusTiC.materials.put("ghostlystone", ghostlyStone);
		}
	}
	
	@Override
	public void init2() {
		Material ghostlyStone = PlusTiC.materials.get("ghostlystone");
		if (ghostlyStone != null) {
			Item ghostlyStoneItem = Item.REGISTRY.getObject(new ResourceLocation("aoa3:ghostly_stone"));
			ghostlyStone.addItem(ghostlyStoneItem, 1, Material.VALUE_Ingot);
			ghostlyStone.setRepresentativeItem(ghostlyStoneItem);
		}
	}
}
