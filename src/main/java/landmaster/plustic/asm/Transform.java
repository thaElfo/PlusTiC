package landmaster.plustic.asm;

import java.util.*;

import codechicken.lib.asm.*;
import net.minecraft.launchwrapper.*;

/**
 * <strong>I SWEAR ... THOSE ********* WHO PUT ******* IMMUTABLE LISTS AS BLOCK DROPS!!</strong>
 * @author Landmaster
 */
public class Transform implements IClassTransformer {
	private ModularASMTransformer transformer;
	
	public Transform() {
		transformer = new ModularASMTransformer();
		Map<String, ASMBlock> blocks = ASMReader.loadResource("/assets/plustic/asm/patch.asm");
		transformer.add(new ModularASMTransformer.MethodInjector(new ObfMapping(
				"net/minecraft/block/Block",
				"func_180653_a",
				"(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;FI)V"),
				blocks.get("dropBlockAsItemWithChance"), true));
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		return transformer.transform(name, bytes);
	}
}
