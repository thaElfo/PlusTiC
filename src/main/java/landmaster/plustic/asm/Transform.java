package landmaster.plustic.asm;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import com.google.common.collect.*;

import net.minecraft.launchwrapper.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.asm.transformers.deobf.*;

/**
 * Until another transform is needed, this is needed for 1.10 only.
 * Blame Extra Utilities for the transform.
 * @author Landmaster
 */
public class Transform implements IClassTransformer {
	private static String mapMethod(String owner, MethodNode node, boolean obf) {
		return mapMethod(owner, node.name, node.desc, obf);
	}
	
	private static String mapMethod(String owner, String name, String desc, boolean obf) {
		return obf ? FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc) : name;
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
						&& "getDrops".equals(((MethodInsnNode)insn).name));
				
				FMLLog.info("Found method getDrops in dropBlockAsItemWithChance, inserting patch");
				
				InsnList insns = new InsnList();
				insns.add(new InsnNode(Opcodes.DUP));
				insns.add(new TypeInsnNode(Opcodes.INSTANCEOF, "java/util/ArrayList"));
				
				LabelNode label = new LabelNode();
				insns.add(new JumpInsnNode(Opcodes.IFNE, label));
				
				insns.add(new TypeInsnNode(Opcodes.NEW, "java/util/ArrayList"));
				insns.add(new InsnNode(Opcodes.DUP_X1));
				insns.add(new InsnNode(Opcodes.DUP_X1));
				insns.add(new InsnNode(Opcodes.POP));
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
