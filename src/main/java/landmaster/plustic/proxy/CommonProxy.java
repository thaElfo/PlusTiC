package landmaster.plustic.proxy;

import java.util.*;

import landmaster.plustic.*;
import landmaster.plustic.entity.*;
import net.minecraft.client.settings.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.tools.*;

public class CommonProxy {
	@SideOnly(Side.CLIENT)
	public static List<KeyBinding> keyBindings;
	
	public void registerItemRenderer(Item item, int meta, String id) {
	}
	
	public void setRenderInfo(Material mat, int color) {
	}
	
	public void setRenderInfo(Material mat, int lo, int mid, int hi) {
	}
	
	public void registerFluidModels(Fluid fluid) {
	}
	
	public void registerKeyBindings() {
	}
	
	public void registerToolModel(ToolCore tc) {
	}
	
	public void registerModifierModel(IModifier mod, ResourceLocation rl) {
	}
	
	public void initEntities() {
		int id = 1;
		EntityRegistry.registerModEntity(EntityBlindBandit.class, "BlindBandit", id++, PlusTiC.INSTANCE, 64, 3, true, 0xFF00FF, 0xFF0000);
	}
	
	public void initToolGuis() {
	}
}
