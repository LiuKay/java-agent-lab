package com.kay.tree.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

/**
 * Used to remove a method.
 */
public class RemoveMethodTransformer extends ClassTransformer {
    private String methodName;
    private String methodDesc;

    public RemoveMethodTransformer(ClassTransformer ct, String mName, String mDesc) {
        super(ct);
        this.methodName = mName;
        this.methodDesc = mDesc;
    }

    @Override
    public void transform(ClassNode classNode) {
        final Iterator<MethodNode> iterator = classNode.methods.iterator();
        if (iterator.hasNext()) {
            final MethodNode methodNode = iterator.next();
            if (methodNode.name.equals(methodName) && methodDesc.equals(methodDesc)) {
                iterator.remove();
            }
        }

        super.transform(classNode);
    }
}
