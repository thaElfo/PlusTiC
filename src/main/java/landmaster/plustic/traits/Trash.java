package landmaster.plustic.traits;

import java.util.*;

import org.apache.commons.lang3.tuple.*;

import com.google.common.collect.*;

import landmaster.plustic.api.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;
import slimeknights.tconstruct.shared.*;

public class Trash extends AbstractTrait {
	public static final Trash trash = new Trash();
	
	protected static final List<Pair<Integer, ItemStack>> trashThings
	= ImmutableList.of(
			Pair.of(20, new ItemStack(Items.COAL)),
			Pair.of(5, new ItemStack(Items.SLIME_BALL)),
			Pair.of(10, new ItemStack(Items.SADDLE)),
			Pair.of(5, TinkerCommons.matSlimeBallBlue.copy()),
			Pair.of(1, new ItemStack(Items.EMERALD)),
			Pair.of(3, new ItemStack(Items.MELON))
			);
	protected static final int trashThingsSum = trashThings.stream().mapToInt(Pair::getLeft).sum();
	protected static @javax.annotation.Nullable ItemStack fetchThing() {
		int rval = random.nextInt(trashThingsSum);
		ItemStack thing = null;
		for (Pair<Integer, ItemStack> entry: trashThings) {
			rval -= entry.getLeft();
			thing = entry.getRight();
			if (rval < 0) break;
		}
		return thing;
	}
	
	public Trash() {
		super("trash", 0x005500);
		Toggle.toggleable.add(identifier);
	}
	
	@Override
	public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!world.isRemote && world instanceof WorldServer
				&& ToolHelper.getCurrentDurability(tool) >= 1 && isSelected
				&& Toggle.getToggleState(tool, identifier) && random.nextFloat() < 0.01f) {
			IItemHandler handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if (handler != null) {
				ItemStack thing = fetchThing();
				if (thing != null && thing.stackSize > 0) {
					thing = thing.copy();
					for (int i=0; i<handler.getSlots(); ++i) {
						thing = handler.insertItem(i, thing, false);
						if (thing == null) {
							EntityLivingBase entityLiving = entity instanceof EntityLivingBase ?
									(EntityLivingBase)entity : FakePlayerFactory.getMinecraft((WorldServer)world);
							ToolHelper.damageTool(tool, 1, entityLiving);
							break;
						}
					}
				}
			}
		}
	}
}
