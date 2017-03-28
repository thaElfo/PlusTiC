package landmaster.plustic.traits;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.nbt.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class Botanical extends AbstractTraitLeveled {
	public static final List<Botanical> botanical = ImmutableList.of(new Botanical(1), new Botanical(2));
	
	public Botanical(int levels) {
		super("botanical", 0x00FF00, 3, levels);
	}
	
	@Override
	public void applyModifierEffect(NBTTagCompound rootCompound) {
		NBTTagCompound toolTag = TagUtil.getToolTag(rootCompound);
		int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS) + 1<<(levels-1);
		toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));
		TagUtil.setToolTag(rootCompound, toolTag);
	}
}
