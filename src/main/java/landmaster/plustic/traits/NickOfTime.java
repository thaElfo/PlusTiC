package landmaster.plustic.traits;

import landmaster.plustic.api.*;
import landmaster.plustic.net.*;
import landmaster.plustic.proxy.*;
import landmaster.plustic.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.items.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class NickOfTime extends AbstractTrait {
	public static final NickOfTime nickOfTime = new NickOfTime();
	
	public static final int ENDER_COST = 8;
	
	public NickOfTime() {
		super("nickoftime", 0xFFF98E);
		MinecraftForge.EVENT_BUS.register(this);
		Toggle.toggleable.add(identifier);
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (isSelected && FMLCommonHandler.instance().getSide().isClient()) {
			if (CommonProxy.keyBindings.get(2).isPressed()) {
				PacketHandler.INSTANCE.sendToServer(new PacketSetPortal());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void timing(LivingHurtEvent event) {
		NBTTagCompound nbt = TagUtil.getTagSafe(event.getEntityLiving().getHeldItemMainhand());
		Coord4D coord;
		if (!event.getEntity().getEntityWorld().isRemote
				&& event.getEntity() instanceof EntityPlayerMP
				&& event.getEntityLiving().getHealth() <= event.getAmount()
				&& TinkerUtil.hasTrait(nbt, identifier)
				&& Toggle.getToggleState(nbt, identifier)
				&& nbt.hasKey("nickoftime", 10)
				&& canTeleport((EntityPlayer)event.getEntity(),
						(coord = Coord4D.fromNBT(nbt.getCompoundTag("nickoftime"))))) {
			IItemHandler ih = event.getEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for (int i=0; i<ih.getSlots(); ++i) {
				ItemStack is = ih.extractItem(i, ENDER_COST, true);
				if (is != null && is.getItem() == Items.ENDER_PEARL) {
					teleportPlayerTo((EntityPlayerMP)event.getEntity(), coord);
					ih.extractItem(i, ENDER_COST, false);
					event.setCanceled(true);
					event.getEntity().addChatMessage(new TextComponentTranslation(
							"msg.plustic.nickmodifier.use"));
					return;
				}
			}
		}
	}
	
	private boolean canTeleport(EntityPlayer player, Coord4D dest) {
		if (dest == null) return false;
		for (int i=1; i<=2; ++i) {
			if (dest.add(0, i, 0).blockState().getBlock() != Blocks.AIR) {
				return false;
			}
		}
		return true;
	}
	
	public static void teleportPlayerTo(EntityPlayerMP player, Coord4D coord) {
		if (player.dimension != coord.dimensionId) {
			int id = player.dimension;
			WorldServer oldWorld = player.mcServer.worldServerForDimension(player.dimension);
			player.dimension = coord.dimensionId;
			WorldServer newWorld = player.mcServer.worldServerForDimension(player.dimension);
			player.connection.sendPacket(new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), newWorld.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
			oldWorld.removeEntityDangerously(player);
			player.isDead = false;

			if(player.isEntityAlive())
			{
				newWorld.spawnEntityInWorld(player);
				player.setLocationAndAngles(coord.xCoord+0.5, coord.yCoord+1, coord.zCoord+0.5, player.rotationYaw, player.rotationPitch);
				newWorld.updateEntityWithOptionalForce(player, false);
				player.setWorld(newWorld);
			}

			player.mcServer.getPlayerList().preparePlayer(player, oldWorld);
			player.connection.setPlayerLocation(coord.xCoord+0.5, coord.yCoord+1, coord.zCoord+0.5, player.rotationYaw, player.rotationPitch);
			player.interactionManager.setWorld(newWorld);
			player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, newWorld);
			player.mcServer.getPlayerList().syncPlayerInventory(player);

			for(PotionEffect potioneffect : player.getActivePotionEffects())
			{
				player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
			}

			player.connection.sendPacket(new SPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel)); // Force XP sync

			FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, id, coord.dimensionId);
		}
		else {
			player.connection.setPlayerLocation(coord.xCoord+0.5, coord.yCoord+1, coord.zCoord+0.5, player.rotationYaw, player.rotationPitch);
		}
	}
}
