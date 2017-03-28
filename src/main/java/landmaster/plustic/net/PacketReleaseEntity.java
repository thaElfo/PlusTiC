package landmaster.plustic.net;

import io.netty.buffer.*;
import landmaster.plustic.traits.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import slimeknights.tconstruct.library.utils.*;

public class PacketReleaseEntity implements IMessage, IMessageHandler<PacketReleaseEntity, IMessage> {
	@Override
	public IMessage onMessage(PacketReleaseEntity message, MessageContext ctx) {
		IThreadListener mainThread = (WorldServer)ctx.getServerHandler().playerEntity.getEntityWorld();
		mainThread.addScheduledTask(() -> {
			EntityPlayer ep = ctx.getServerHandler().playerEntity;
			Vec3d eye = ep.getPositionVector().addVector(0, ep.getEyeHeight(), 0);
			Vec3d look = ep.getLookVec().scale(5);
			RayTraceResult rtr = ep.getEntityWorld().rayTraceBlocks(eye, eye.add(look));
			NBTTagCompound nbt = TagUtil.getTagSafe(ep.getHeldItemMainhand());
			if (ep.getEntityWorld().isRemote // for good measure
					|| rtr == null || rtr.sideHit == null
					|| ep.getHeldItemMainhand() == null
					|| !TinkerUtil.hasTrait(nbt, Portly.portly.getIdentifier())
					|| !nbt.hasKey("portlyGentleman", 10)) return;
			Entity ent = EntityList.createEntityFromNBT(
					nbt.getCompoundTag("portlyGentleman"), ep.getEntityWorld());
			if (ent == null) return;
			int offsetX = rtr.sideHit.getFrontOffsetX();
			int offsetY = rtr.sideHit == EnumFacing.DOWN ? -1 : 0;
			int offsetZ = rtr.sideHit.getFrontOffsetZ();
			AxisAlignedBB bb = ent.getEntityBoundingBox();
			ent.setLocationAndAngles(
					rtr.hitVec.xCoord+(bb.maxX-bb.minX)*0.5*offsetX,
					rtr.hitVec.yCoord+(bb.maxY-bb.minY)*0.5*offsetY,
					rtr.hitVec.zCoord+(bb.maxZ-bb.minZ)*0.5*offsetZ,
					ep.getEntityWorld().rand.nextFloat()*360, 0);
			if (!ep.getEntityWorld().spawnEntityInWorld(ent)) return;
			if (ent instanceof EntityLiving) ((EntityLiving)ent).playLivingSound();
			String id = nbt.getCompoundTag("portlyGentleman").getString("id");
			nbt.removeTag("portlyGentleman");
			ep.getHeldItemMainhand().setTagCompound(nbt);
			ep.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
			ep.swingArm(EnumHand.MAIN_HAND);
			ep.addChatMessage(new TextComponentTranslation(
					"msg.plustic.portlymodifier.unset", id));
		});
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}
}
