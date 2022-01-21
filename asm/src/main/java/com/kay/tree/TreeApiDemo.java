package com.kay.tree;

import com.kay.Utils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.V11;

/**
 * Using the tree API to generate a class takes about 30% more time (see Appendix
 * A.1) and consumes more memory than using the core API. But it
 * makes it possible to generate the class elements in any order, which can be
 * convenient in some cases.
 */
public class TreeApiDemo {
    public static void main(String[] args) {
        final ClassNode classNode = new ClassNode();
        classNode.version = V11;
        classNode.access = ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE;
        classNode.name = "com/kay/Comparable";
        classNode.superName = "java/lang/Object";
        classNode.fields.add(new FieldNode(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                                    "LESS", "I", null, Integer.valueOf(-1)));
        classNode.fields.add(new FieldNode(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                                    "EQUAL", "I", null, Integer.valueOf(0)));
        classNode.fields.add(new FieldNode(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                                    "GREATER", "I", null, Integer.valueOf(1)));
        classNode.methods.add(new MethodNode(ACC_PUBLIC + ACC_ABSTRACT,
                                      "compareTo", "(Ljava/lang/Object;)I", null, null));


        final ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);

        Utils.outputClass(writer.toByteArray(), "Comparable");
    }
}
