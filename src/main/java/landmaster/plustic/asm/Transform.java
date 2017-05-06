package landmaster.plustic.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import net.minecraft.launchwrapper.*;
import net.minecraftforge.fml.common.asm.transformers.deobf.*;

/**
 * <strong>I SWEAR … THOSE ********* WHO PUT ******* IMMUTABLE LISTS AS BLOCK DROPS!!
 * ESPECIALLY YOU, EXTRA UTILITIES!!</strong>
 * @author Landmaster
 */
public class Transform implements IClassTransformer {
	private static String mapMethod(String owner, MethodNode node, boolean obf) {
		return obf ? FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, node.name, node.desc) : node.name;
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (transformedName.equals("net.minecraft.block.Block")) {
			boolean isObfuscated = !name.equals(transformedName);
			
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, 0);
			
			classNode.methods.stream()
			.filter(methodNode -> mapMethod(name, methodNode, isObfuscated).equals("func_180653_a"))
			.forEach(methodNode -> {
				System.out.println("Patching method dropBlockAsItemWithChance");
				
				// clear everything
				methodNode.instructions.clear();
				
				// load variables
				methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
				methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
				methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
				methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
				methodNode.instructions.add(new VarInsnNode(Opcodes.FLOAD, 4));
				methodNode.instructions.add(new VarInsnNode(Opcodes.ILOAD, 5));
				
				// patch method
				methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
						"landmaster/plustic/asm/Patches",
						"dropBlockAsItemWithChance",
						"(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;FI)V",
						false));
				
				methodNode.instructions.add(new InsnNode(Opcodes.RETURN));
			});
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			classNode.accept(classWriter);
			return classWriter.toByteArray();
		}
		return bytes;
	}
}
