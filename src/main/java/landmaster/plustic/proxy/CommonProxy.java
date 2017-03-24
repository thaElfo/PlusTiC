package landmaster.plustic.proxy;

import java.util.*;

import landmaster.plustic.*;
import landmaster.plustic.entity.*;
import net.minecraft.client.settings.*;
import net.minecraft.item.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;
import slimeknights.tconstruct.library.materials.*;

public class CommonProxy {
	@SideOnly(Side.CLIENT)
	public static List<KeyBinding> keyBindings;
	
	public void registerItemRenderer(Item item, int meta, String id) {
	}
	
	public void setRenderInfo(Material mat, int color) {
	}
	
	public void registerFluidModels(Fluid fluid) {
	}
	
	public void registerKeyBindings() {
	}
	
	public void initEntities() {
		int id = 1;
		EntityRegistry.registerModEntity(EntityBlindBandit.class, "BlindBandit", id++, PlusTiC.INSTANCE, 64, 3, true, 0xFF00FF, 0xFF0000);
	}
}
