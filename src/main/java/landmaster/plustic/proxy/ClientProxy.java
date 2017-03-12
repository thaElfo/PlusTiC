package landmaster.plustic.proxy;

import java.util.*;
import javax.annotation.*;
import landmaster.plustic.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.client.settings.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.client.model.*;
import org.lwjgl.input.*;
import slimeknights.tconstruct.library.materials.*;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
	    ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(PlusTiC.MODID + ":" + id, "inventory"));
	}
	
	@Override
	public void setRenderInfo(Material mat, int color) {
		mat.setRenderInfo(color);
	}
	
	@Override
	public void registerFluidModels(Fluid fluid) {
		if (fluid == null) return;
		Block block = fluid.getBlock();
		if (block != null) {
			Item item = Item.getItemFromBlock(block);
			FluidStateMapper mapper = new FluidStateMapper(fluid);
			if (item != null) {
				ModelBakery.registerItemVariants(item);
				ModelLoader.setCustomMeshDefinition(item, mapper);
			}
			ModelLoader.setCustomStateMapper(block, mapper);
		}
	}
	
	@Override
	public void registerKeyBindings() {
		keyBindings = Arrays.asList(
				new KeyBinding("key.plustic_release_entity.desc", Keyboard.KEY_0, "key.categories.plustic"),
				new KeyBinding("key.plustic_toggle_gui.desc", Keyboard.KEY_I, "key.categories.plustic"),
				new KeyBinding("key.plustic_set_portal.desc", Keyboard.KEY_N, "key.categories.plustic"));
		for (KeyBinding kb: keyBindings) ClientRegistry.registerKeyBinding(kb);
	}
	
	public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {
		public final Fluid fluid;
		public final ModelResourceLocation location;
		
		public FluidStateMapper(Fluid fluid) {
			this.fluid = fluid;
			this.location = new ModelResourceLocation(new ResourceLocation(PlusTiC.MODID, "fluid_block"),
					fluid.getName());
		}
		
		@Nonnull
		@Override
		protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
			return location;
		}
		
		@Nonnull
		@Override
		public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
			return location;
		}
	}
}
