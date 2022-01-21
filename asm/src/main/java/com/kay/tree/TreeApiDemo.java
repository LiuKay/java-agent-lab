package com.kay.tree;

import com.kay.Utils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
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
        classNode.methods.add(new MethodNode(ACC_PUBLIC + ACC_ABSTRACT,
                                      "compareTo", "(Ljava/lang/Object;)I", null, null));


        final ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);

        //output the bytes into a .class file
        Utils.outputClass(writer.toByteArray(), "Comparable");
    }
}
