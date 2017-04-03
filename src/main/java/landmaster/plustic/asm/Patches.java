package landmaster.plustic.asm;

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class Patches {
	public static void dropBlockAsItemWithChance(Block block, World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            java.util.List<ItemStack> items = new ArrayList<>(block.getDrops(worldIn, pos, state, fortune)); // wrap in array list as patch
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, block.harvesters.get());

            for (ItemStack item : items)
            {
                if (worldIn.rand.nextFloat() <= chance)
                {
                    Block.spawnAsEntity(worldIn, pos, item);
                }
            }
        }
	}
}
