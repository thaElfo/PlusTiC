package landmaster.plustic.asm;

import java.util.*;

import com.google.common.base.*;

import java.lang.reflect.*;

import landmaster.plustic.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class Patches {
	private static final Set<Block> warned = new HashSet<>();
	
	public static void dropBlockAsItemWithChance(Block block, World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            java.util.List<ItemStack> items = block.getDrops(worldIn, pos, state, fortune);
            if (!(items instanceof ArrayList)) {
            	if (!warned.contains(block)) {
            		PlusTiC.log.warn(String.format(Locale.US, "Block '%s' should return a 'java.util.ArrayList' for 'getDrops'; got a '%s' instead", block.getUnlocalizedName(), items.getClass().getName()));
            		warned.add(block);
            	}
            	items = new ArrayList<>(items); // wrap in array list as patch
            }
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvester(block));

            for (ItemStack item : items)
            {
                if (worldIn.rand.nextFloat() <= chance)
                {
                    Block.spawnAsEntity(worldIn, pos, item);
                }
            }
        }
	}
	
	private static Field harvestersF;
	@SuppressWarnings("unchecked")
	private static EntityPlayer harvester(Block block) {
		try {
			if (harvestersF == null) {
				harvestersF = Block.class.getDeclaredField("harvesters");
				harvestersF.setAccessible(true);
			}
			return ((ThreadLocal<EntityPlayer>)harvestersF.get(block)).get();
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
}
