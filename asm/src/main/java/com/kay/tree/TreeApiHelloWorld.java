package com.kay.tree;

import com.kay.Utils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_8;

public class TreeApiHelloWorld {

    public static void main(String[] args) {
        ClassNode classNode = new ClassNode();
        classNode.name = "com/kay/AsmTreeHelloWorld";
        classNode.version = V1_8;
        classNode.access = ACC_PUBLIC;
        classNode.superName = "java/lang/Object";

        //<init>
        final MethodNode mn = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        final InsnList insnList = mn.instructions;
        insnList.add(new VarInsnNode(ALOAD, 0));
        insnList.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false));
        insnList.add(new InsnNode(RETURN));
        mn.maxLocals = 1;
        mn.maxStack = 1;
        classNode.methods.add(mn);

        //main
        final MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null,
                                                     null);
        final InsnList instructions = methodNode.instructions;

        instructions.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStrem"));
        instructions.add(new LdcInsnNode("Hello World ASM!"));
        instructions.add(
                new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
        instructions.add(new InsnNode(RETURN));
        methodNode.maxStack = 2;
        methodNode.maxLocals = 1;
        classNode.methods.add(methodNode);

        final ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);

        Utils.outputClass(classWriter.toByteArray(), "AsmTreeHelloWorld");
    }
}
