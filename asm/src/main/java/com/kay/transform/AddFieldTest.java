package com.kay.transform;

import com.kay.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

public class AddFieldTest extends ClassLoader {

    public static void main(String[] args) throws IOException, NoSuchFieldException {
        final ClassReader classReader = new ClassReader(Person.class.getName());
        final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
        //use this util to check the class is valid
        CheckClassAdapter classAdapter = new CheckClassAdapter(classWriter);

        //add field "public String address"
        final FieldAdapter fieldAdapter = new FieldAdapter(classAdapter, Opcodes.ACC_PUBLIC, "address",
                                                           Type.getDescriptor(String.class));
        classReader.accept(fieldAdapter, ClassReader.EXPAND_FRAMES);
        final byte[] bytes = classWriter.toByteArray();

        Utils.outputClass(bytes, "Person");
        final Class<?> clazz = new AddFieldTest().defineClass("com.kay.transform.Person", bytes, 0, bytes.length);

        final Field address = clazz.getField("address");
        final Class<?> type = address.getType();
        System.out.println(type);
    }

    static class FieldAdapter extends ClassVisitor {
        private int access;
        private String fName;
        private String fDesc;

        private boolean isPresent;

        public FieldAdapter(ClassVisitor classVisitor, int access, String name, String desc) {
            super(Opcodes.ASM6, classVisitor);
            this.access = access;
            this.fName = name;
            this.fDesc = desc;
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            if (name.equals(fName)) {
                this.isPresent = true;
            }
            return super.visitField(access, name, descriptor, signature, value);
        }

        @Override
        public void visitEnd() {
            if (!isPresent) {
                final FieldVisitor fieldVisitor = cv.visitField(access, fName, fDesc, null, null);
                if (fieldVisitor != null) {
                    fieldVisitor.visitEnd();
                }
            }
            super.visitEnd();
        }
    }
}
