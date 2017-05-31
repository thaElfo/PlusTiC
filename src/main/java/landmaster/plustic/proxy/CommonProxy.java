package landmaster.plustic.proxy;

import landmaster.plustic.*;
import landmaster.plustic.entity.*;
import mcjty.lib.tools.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.fluids.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.tools.*;

public class CommonProxy {
	
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
	
	public <T extends Item & IToolPart> void registerToolPartModel(T part) {
	}
	
	public void initEntities() {
		EntityTools.registerModEntity(new ResourceLocation(PlusTiC.MODID, "blindbandit"), EntityBlindBandit.class, "BlindBandit", 1, PlusTiC.INSTANCE, 64, 3, true, 0xFF00FF, 0xFF0000);
	}
	
	public void initToolGuis() {
	}
	
	public boolean isControlPressed(String control) {
		return false;
	}
}
