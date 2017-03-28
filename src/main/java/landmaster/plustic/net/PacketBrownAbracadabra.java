package landmaster.plustic.net;

import io.netty.buffer.*;
import landmaster.plustic.api.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import slimeknights.tconstruct.library.utils.*;

public class PacketBrownAbracadabra implements IMessage, IMessageHandler<PacketBrownAbracadabra, IMessage> {
	@Override
	public IMessage onMessage(PacketBrownAbracadabra message, MessageContext ctx) {
		IThreadListener mainThread = (WorldServer)ctx.getServerHandler().playerEntity.getEntityWorld();
		mainThread.addScheduledTask(() -> {
			EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
			if (ep.getEntityWorld().isRemote)
				return;
			NBTTagCompound nbt = TagUtil.getTagSafe(ep.getHeldItemMainhand());
			Coord4D coord = Coord4D.fromNBT(nbt.getCompoundTag(Portal.PORTAL_NBT));
			if (TinkerUtil.hasTrait(nbt, BrownMagic.brownmagic.identifier)
					&& Utils.canTeleportTo(ep, coord)) {
				ep.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
				Utils.teleportPlayerTo(ep, coord);
				ep.addChatMessage(new TextComponentTranslation("msg.plustic.brownmagic.use"));
			}
		});
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}
}
