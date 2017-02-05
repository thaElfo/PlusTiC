package landmaster.plustic.block;

import net.minecraft.block.*;
import net.minecraft.block.material.*;

public class MetalBlock extends Block {
	public MetalBlock(String name) {
		super(Material.IRON);
		this.setHarvestLevel("pickaxe", -1);
		this.setHardness(5);
		this.setUnlocalizedName(name).setRegistryName(name);
	}
}
