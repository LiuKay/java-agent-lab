package com.kay.tree.transformer;

import org.objectweb.asm.tree.ClassNode;

public class ClassTransformer {

    protected ClassTransformer ct;

    public ClassTransformer(ClassTransformer ct) {
        this.ct = ct;
    }

    public void transform(ClassNode classNode) {
        if (classNode != null) {
            ct.transform(classNode);
        }
    }
}
