package landmaster.plustic.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import com.google.common.collect.*;

import net.minecraft.launchwrapper.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.asm.transformers.deobf.*;

/**
 * <strong>I SWEAR … THOSE ********* WHO PUT ******* IMMUTABLE LISTS AS BLOCK DROPS!!
 * ESPECIALLY YOU, EXTRA UTILITIES!!</strong>
 * @author Landmaster
 */
public class Transform implements IClassTransformer {
	private static String mapMethod(String owner, MethodNode node, boolean obf) {
		return mapMethod(owner, node.name, node.desc, obf);
	}
	
	private static String mapMethod(String owner, String name, String desc, boolean obf) {
		return obf ? FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc) : name;
	}
	
	private static String mapClass(String name, boolean obf) {
		return obf ? FMLDeobfuscatingRemapper.INSTANCE.map(name) : name;
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
				FMLLog.info("Patching method dropBlockAsItemWithChance");
				
				AbstractInsnNode insnPos = Iterables.find(methodNode.instructions::iterator,
						insn -> insn.getOpcode() == Opcodes.INVOKEVIRTUAL
						&& mapMethod(mapClass("net/minecraft/block/Block", isObfuscated),
								"getDrops",
								"(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet.minecrat.block.state.IBlockState;I)Ljava/util/List;",
								isObfuscated).equals(((MethodInsnNode)insn).name));
				
				InsnList insns = new InsnList();
				insns.add(new InsnNode(Opcodes.DUP));
				insns.add(new TypeInsnNode(Opcodes.INSTANCEOF, "java/util/ArrayList"));
				
				LabelNode label = new LabelNode();
				insns.add(new JumpInsnNode(Opcodes.IFNE, label));
				
				insns.add(new VarInsnNode(Opcodes.ASTORE, 6));
				insns.add(new TypeInsnNode(Opcodes.NEW, "java/util/ArrayList"));
				insns.add(new InsnNode(Opcodes.DUP));
				insns.add(new VarInsnNode(Opcodes.ALOAD, 6));
				insns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "(Ljava/util/Collection;)V", false));
				
				insns.add(new FrameNode(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/util/ArrayList"}));
				insns.add(label);
				
				methodNode.instructions.insert(insnPos, insns);
			});
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			classNode.accept(classWriter);
			return classWriter.toByteArray();
		}
		return bytes;
	}
}
