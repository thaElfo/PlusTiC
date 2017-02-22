package landmaster.plustic.traits;

import java.util.*;
import net.minecraft.client.resources.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.items.*;
import slimeknights.tconstruct.library.tools.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class Global extends AbstractTrait {
	public static final Global global = new Global();
	
	public Global() {
		super("global", 0xFFE0F1);
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void blockDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getWorld().isRemote
				|| event.getHarvester() == null) return;
		ItemStack tool = DualToolHarvestUtils.getItemstackToUse(event.getHarvester(), event.getState());
		if (!TinkerUtil.hasTrait(TagUtil.getTagSafe(tool), getIdentifier())) return;
		__blockHarvestDrops(tool, event);
	}
	private void __blockHarvestDrops(ItemStack tool, BlockEvent.HarvestDropsEvent event) {
		NBTTagCompound nbt0 = TagUtil.getTagSafe(tool);
		if (nbt0.hasKey("global", 10) && ToolHelper.isToolEffective2(tool, event.getState())) {
			NBTTagCompound nbt = nbt0.getCompoundTag("global");
			WorldServer world = DimensionManager.getWorld(nbt.getInteger("dim"));
			if (world == null) return;
			TileEntity te = world.getTileEntity(new BlockPos(
					nbt.getInteger("x"),
					nbt.getInteger("y"),
					nbt.getInteger("z")
					));
			if (te == null) return;
			IItemHandler ih = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					EnumFacing.VALUES[nbt.getByte("facing")]);
			if (ih == null) return;
			ListIterator<ItemStack> it = event.getDrops().listIterator();
			while (it.hasNext()) {
				ItemStack stk = it.next();
				for (int j=0; j<ih.getSlots(); ++j) {
					ItemStack res = ih.insertItem(j, stk, false);
					if (res != null && res.stackSize > 0) {
						it.set(res);
						stk = res;
					} else {
						it.remove();
						break;
					}
				}
			}
			te.markDirty();
		}
	}
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void dropEvent(LivingDropsEvent event) {
		World world0 = event.getEntity().worldObj;
		if (world0.isRemote
				|| event.getEntityLiving().getHealth() > 0) return;
		ItemStack weapon = getWeapon(event.getSource());
		NBTTagCompound nbt0 = TagUtil.getTagSafe(weapon);
		if (TinkerUtil.hasTrait(nbt0, getIdentifier())) {
			if (nbt0.hasKey("global", 10)) {
				NBTTagCompound nbt = nbt0.getCompoundTag("global");
				WorldServer world = DimensionManager.getWorld(nbt.getInteger("dim"));
				if (world == null) return;
				TileEntity te = world.getTileEntity(new BlockPos(
						nbt.getInteger("x"),
						nbt.getInteger("y"),
						nbt.getInteger("z")
						));
				if (te == null) return;
				IItemHandler ih = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
						EnumFacing.VALUES[nbt.getByte("facing")]);
				if (ih == null) return;
				ListIterator<EntityItem> it = event.getDrops().listIterator();
				while (it.hasNext()) {
					EntityItem enti = it.next();
					ItemStack stk = enti.getEntityItem();
					for (int j=0; j<ih.getSlots(); ++j) {
						ItemStack res = ih.insertItem(j, stk, false);
						if (res != null && res.stackSize > 0) {
							enti.setEntityItemStack(res);
							stk = res;
						} else {
							it.remove();
							break;
						}
					}
				}
				te.markDirty();
			}
		}
	}
	@SubscribeEvent
	public void onPlayerUse(PlayerInteractEvent.RightClickBlock event) {
		NBTTagCompound nbt = TagUtil.getTagSafe(event.getItemStack());
		if (event.getWorld().isRemote
				|| event.isCanceled()
				|| !event.getEntityPlayer().isSneaking()
				|| event.getItemStack() == null
				|| event.getFace() == null
				|| event.getWorld().getTileEntity(event.getPos()) == null
				|| !TinkerUtil.hasTrait(nbt, getIdentifier()))
			return;
		NBTTagCompound global = new NBTTagCompound();
		global.setInteger("x", event.getPos().getX());
		global.setInteger("y", event.getPos().getY());
		global.setInteger("z", event.getPos().getZ());
		global.setInteger("dim", event.getWorld().provider.getDimension());
		global.setByte("facing", (byte)event.getFace().ordinal());
		nbt.setTag("global", global);
		event.getItemStack().setTagCompound(nbt);
		event.getEntityPlayer().addChatMessage(new TextComponentTranslation(
				"msg.plustic.globalmodifier.set",
						event.getPos().getX(),
						event.getPos().getY(),
						event.getPos().getZ(),
						event.getWorld().provider.getDimension()));
	}
	@SubscribeEvent
	public void tooltip(ItemTooltipEvent event) {
		NBTTagCompound nbt0 = TagUtil.getTagSafe(event.getItemStack());
		if (event.isCanceled()
				|| event.getItemStack() == null
				|| !FMLCommonHandler.instance().getSide().isClient()
				|| !TinkerUtil.hasTrait(nbt0, getIdentifier())) return;
		if (nbt0.hasKey("global", 10)) {
			NBTTagCompound nbt = nbt0.getCompoundTag("global");
			event.getToolTip().add(I18n.format("tooltip.plustic.globalmodifier.info",
					nbt.getInteger("x"),
					nbt.getInteger("y"),
					nbt.getInteger("z"),
					nbt.getInteger("dim")));
		}
	}
	private ItemStack getWeapon(DamageSource source) {
		if (source instanceof EntityDamageSource) {
			Entity entity = ((EntityDamageSource) source).getEntity();
			if (entity instanceof EntityPlayer)
				return ((EntityPlayer) entity).getHeldItemMainhand();
		}
		return null;
	}

}
