package landmaster.plustic.entity.render;

import javax.annotation.*;

import landmaster.plustic.*;
import landmaster.plustic.entity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.client.registry.*;

public class RenderBlindBandit extends RenderLiving<EntityBlindBandit> {
	public static final Factory FACTORY = new Factory();
	public static final ResourceLocation tex = new ResourceLocation(PlusTiC.MODID+":textures/entity/blindbandit.png");;
	
	public RenderBlindBandit(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBlindBandit(), 0.5F);
	}
	
	@Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityBlindBandit ent) {
		return tex;
	}
	
	public static class Factory implements IRenderFactory<EntityBlindBandit> {
        @Override
        public Render<? super EntityBlindBandit> createRenderFor(RenderManager manager) {
            return new RenderBlindBandit(manager);
        }
    }
}
