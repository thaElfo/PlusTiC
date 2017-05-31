package landmaster.plustic.api;

import java.util.*;

import landmaster.plustic.*;
import landmaster.plustic.net.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.*;
import net.minecraft.nbt.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.relauncher.*;
import slimeknights.tconstruct.library.utils.*;

public class Portal {
	static {
		MinecraftForge.EVENT_BUS.register(Portal.class);
	}
	
	public static final String PORTAL_NBT = "nickoftime";
	
	public static final Set<String> portalable = new HashSet<>();
	
	public static boolean canUse(NBTTagCompound nbt) {
		for (String identifier: portalable) {
			if (TinkerUtil.hasTrait(nbt, identifier)) {
				return true;
			}
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void testSetPortal(InputEvent.KeyInputEvent event) {
		if (PlusTiC.proxy.isControlPressed("set_portal")
				&& canUse(TagUtil.getTagSafe(
						Minecraft.getMinecraft().player.getHeldItemMainhand()))) {
			PacketHandler.INSTANCE.sendToServer(new PacketSetPortal());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent event) {
		NBTTagCompound nbt0 = TagUtil.getTagSafe(event.getItemStack());
		if (event.isCanceled()
				|| event.getItemStack() == null
				|| !canUse(nbt0)) return;
		if (nbt0.hasKey(Portal.PORTAL_NBT, 10)) {
			NBTTagCompound nbt = nbt0.getCompoundTag(Portal.PORTAL_NBT);
			event.getToolTip().add(I18n.format("tooltip.plustic.portal.info",
					nbt.getInteger("x"),
					nbt.getInteger("y"),
					nbt.getInteger("z"),
					nbt.getInteger("dim")));
		}
	}
}
