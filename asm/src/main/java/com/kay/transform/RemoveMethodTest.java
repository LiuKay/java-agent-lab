package com.kay.transform;

import com.kay.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;

public class RemoveMethodTest extends ClassLoader {

    public static void main(String[] args) throws IOException {

        final ClassReader classReader = new ClassReader(Person.class.getName());
        final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        //remove the method : String sayHi(String name)
        classReader.accept(new RemoveMethodAdapter(classWriter, "sayHi",
                                                   Type.getMethodDescriptor(Type.getType(String.class),
                                                                            Type.getType(String.class))), 0);
        final byte[] bytes = classWriter.toByteArray();

        Utils.outputClass(bytes, "Person");

        final Class<?> clazz = new RemoveMethodTest().defineClass("com.kay.transform.Person", bytes, 0, bytes.length);

        try {
            clazz.getMethod("sayHi", String.class);
        } catch (NoSuchMethodException e) {
            System.out.println("Not found method:" + e.getMessage());
        }

    }

    static class RemoveMethodAdapter extends ClassVisitor {
        private String methodName;
        private String methodDesc;

        public RemoveMethodAdapter(ClassVisitor classVisitor,String methodName, String methodDesc) {
            super(Opcodes.ASM6, classVisitor);
            this.methodName = methodName;
            this.methodDesc = methodDesc;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                         String[] exceptions) {
            if (name.equals(methodName) && descriptor.equals(methodDesc)) {
                // not delegate to next visitor. -> remove this method
                System.out.println(String.format("ignore method: name=%s, desc=%s", name, descriptor));
                return null;
            }

            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }

}
