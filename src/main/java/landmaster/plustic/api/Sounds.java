package landmaster.plustic.api;

import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class Sounds {
	public static final SoundEvent LASER_BEAM = new SoundEvent(new ResourceLocation(ModInfo.MODID, "laser_beam"))
			.setRegistryName("laser_beam");
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(LASER_BEAM);
	}
	
	public static void playSoundToAll(Entity entity, SoundEvent sound, float volume, float pitch) {
		entity.getEntityWorld().playSound(null, entity.posX, entity.posY, entity.posZ,
				sound, entity.getSoundCategory(), volume, pitch);
	}
}
