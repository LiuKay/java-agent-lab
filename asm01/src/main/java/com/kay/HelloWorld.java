package com.kay;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * package com.kay;
 *
 * public class AsmHello {
 *     public AsmHello() {
 *     }
 *
 *     public static void main(String[] var0) {
 *         System.out.println("Hello World ASM!");
 *     }
 * }
 */
public class HelloWorld extends ClassLoader{

    public static void main(String[] args) throws Exception {
        final byte[] bytes = generate();
        outputClazz(bytes);

        final Class<?> clazz = new HelloWorld().defineClass("com.kay.AsmHello", bytes, 0, bytes.length);

        final Method main = clazz.getMethod("main", String[].class);
        main.invoke(null, new Object[]{new String[]{}});
    }

    private static byte[] generate(){
        ClassWriter classWriter = new ClassWriter(0);

        //<init>
        {
            classWriter.visit(Opcodes.V11, Opcodes.ACC_PUBLIC, "com/kay/AsmHello", null, "java/lang/Object", null);
            MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(Opcodes.RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }

        //main
        {
            MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "main",
                                                                  "([Ljava/lang/String;)V", null, null);

            methodVisitor.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");

            methodVisitor.visitLdcInsn("Hello World ASM!");

            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                                          "(Ljava/lang/String;)V", false);

            methodVisitor.visitInsn(Opcodes.RETURN);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }

    private static void outputClazz(byte[] bytes){
        try (FileOutputStream out = new FileOutputStream("AsmHello.class")) {
            System.out.println("ASM output path:" + (new File("")).getAbsolutePath());
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
