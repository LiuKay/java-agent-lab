package com.kay.tree.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * Used to add a new filed to a classNode
 */
public class AddFieldTransformer extends ClassTransformer {
    private int access;
    private String name;
    private String desc;

    public AddFieldTransformer(ClassTransformer ct, int access, String name, String desc) {
        super(ct);
        this.access = access;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void transform(ClassNode classNode) {
        boolean isPresent = false;
        for (FieldNode field : classNode.fields) {
            if (field.name.equals(name)) {
                isPresent = true;break;
            }
        }

        if (!isPresent) {
            classNode.fields.add(new FieldNode(access, name, desc, null, null));
        }

        super.transform(classNode);
    }
}
