package com.kay.tree;

import com.kay.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ListIterator;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * Add a field "timer" to MockService class to record time.
 */
public class AddTimerFieldDemo {

    public static void main(String[] args) throws IOException {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(MockService.class.getName());
        classReader.accept(classNode, 0); // classNode consume the event from classReader

        //transform
        new AddTimerFieldDemo().transform(classNode);

        final ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);// classWriter consume the event from classNode

        final byte[] bytes = classWriter.toByteArray();
        Utils.outputClass(bytes, "MockService");
        /**
         * package com.kay.tree;
         * public class MockService {
         *     public static long timer;
         *
         *     public MockService() {
         *     }
         *
         *     public void test() {
         *         timer -= System.currentTimeMillis();
         *         System.out.println("execute..");
         *         timer += System.currentTimeMillis();
         *     }
         * }
         */
    }

    /**
     * add a field "timer" to record method execution time.
     */
    public void transform(ClassNode classNode) {

        for (MethodNode method : classNode.methods) {
            if ("<init>".equals(method.name) || "<clinit>".equals(method.name)) {
                continue;
            }

            final InsnList insnList = method.instructions;
            if (insnList.size() == 0) {
                continue;
            }

            final ListIterator<AbstractInsnNode> iterator = insnList.iterator();
            while (iterator.hasNext()) {
                final AbstractInsnNode insnNode = iterator.next();
                final int opcode = insnNode.getOpcode();

                //the end of the method
                if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                    InsnList list = new InsnList();
                    list.add(new FieldInsnNode(GETSTATIC, classNode.name, "timer", "J"));
                    list.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
                    list.add(new InsnNode(LADD));
                    list.add(new FieldInsnNode(PUTSTATIC, classNode.name, "timer", "J"));

                    //insert before the end(return or throw)
                    insnList.insert(insnNode.getPrevious(), list);
                }
            }

            final InsnList list = new InsnList();
            list.add(new FieldInsnNode(GETSTATIC, classNode.name, "timer", "J"));
            list.add(new MethodInsnNode(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
            list.add(new InsnNode(LSUB));
            list.add(new FieldInsnNode(PUTSTATIC, classNode.name, "timer", "J"));

            //Inserts the given instructions at the beginning of this list.
            insnList.insert(list);

            method.maxStack += 4;
        }

        //add a new field
        classNode.fields.add(new FieldNode(ACC_PUBLIC + ACC_STATIC, "timer", "J", null, null));
    }

}
