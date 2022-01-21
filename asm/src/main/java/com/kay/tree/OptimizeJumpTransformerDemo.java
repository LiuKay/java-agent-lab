package com.kay.tree;

import com.kay.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ListIterator;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * this example comes from the ASM document, but I really don't know what optimization it does?
 * So with doing this, we reduce the instructions?
 */
public class OptimizeJumpTransformerDemo {

    public static void main(String[] args) throws IOException {
        final ClassReader classReader = new ClassReader(JumpTargetClass.class.getName());
        final ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        final OptimizeJumpTransformerDemo transformerDemo = new OptimizeJumpTransformerDemo();
        for (MethodNode method : classNode.methods) {
            transformerDemo.transform(method);
        }

        final ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);

        final byte[] bytes = classWriter.toByteArray();
        Utils.outputClass(bytes, "JumpTargetClass");
    }


    /**
     * replace a jump to GOTO label instruction with a jump to label,
     * and that replaces a GOTO to a RETURN instruction with this RETURN instruction.
     */
    public void transform(MethodNode mn) {
        final InsnList insnList = mn.instructions;

        final ListIterator<AbstractInsnNode> iterator = insnList.iterator();

        while (iterator.hasNext()) {
            final AbstractInsnNode next = iterator.next();
            if (next instanceof JumpInsnNode) {
                LabelNode labelNode = ((JumpInsnNode) next).label;
                AbstractInsnNode target;
                //while target == goto 1,replace label with 1
                while (true) {
                    target = labelNode;
                    while (target != null && target.getOpcode() < 0) {
                        target = target.getNext();
                    }

                    if (target != null && target.getOpcode() == Opcodes.GOTO) {
                        labelNode = ((JumpInsnNode) target).label;
                    } else {
                        break;
                    }
                }

                //update target
                ((JumpInsnNode) next).label = labelNode;

                //if possible, replace jump with target instruction
                if (next.getOpcode() == Opcodes.GOTO && target != null) {
                    final int opcode = target.getOpcode();
                    if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                        //replace 'next' with clone of 'target'
                        insnList.set(next, target.clone(null));
                    }
                }
            }
        }

    }

}
