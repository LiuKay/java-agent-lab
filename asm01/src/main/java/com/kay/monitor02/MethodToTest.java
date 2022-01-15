package com.kay.monitor02;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * record the returned result (normal return or exception)
 */
public class MethodToTest extends ClassLoader{

    /**
     * public Integer strToNumber(String str) {
     *         try {
     *             Integer var2 = Integer.parseInt(str);
     *             record("com.kay.monitor02.MethodToTest.strToNumber", (Object)var2);
     *             return var2;
     *         } catch (Exception var3) {
     *             record("Class: [com.kay.monitor02.MethodToTest], Method [strToNumber]", (Throwable)var3);
     *             throw var3;
     *         }
     *     }
     */
    public Integer strToNumber(String str) {
        return Integer.parseInt(str);
    }


    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        final byte[] bytes = new MethodToTest().getBytes(MethodToTest.class.getName());

        outputClazz(bytes);

        final Class<?> clazz = new MethodToTest().defineClass("com.kay.monitor02.MethodToTest", bytes, 0,
                                                               bytes.length);

        final Method strToNumber = clazz.getMethod("strToNumber", String.class);

        final Object r1 = strToNumber.invoke(clazz.getDeclaredConstructor().newInstance(), "123");
        System.out.println("r1=" + r1);

        final Object r2 = strToNumber.invoke(clazz.getDeclaredConstructor().newInstance(), "abc");
        System.out.println("r1=" + r2);

    }

    private byte[] getBytes(String className) throws IOException {
        final ClassReader classReader = new ClassReader(className);
        final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        classReader.accept(new MyClassVisitor(classWriter, MethodToTest.class.getName(), "strToNumber"),
                           ClassReader.EXPAND_FRAMES);

        return classWriter.toByteArray();
    }


    static class MyClassVisitor extends ClassVisitor {

        private String className;
        private String methodName;

        public MyClassVisitor(ClassVisitor classVisitor, String className,
                              String methodName) {
            super(Opcodes.ASM7_EXPERIMENTAL, classVisitor);
            this.className = className;
            this.methodName = methodName;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                         String[] exceptions) {

            if (!methodName.equals(name)) {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

            return new MyMethodVisitor(className, methodName, Opcodes.ASM6, mv, access, name, descriptor);
        }

    }

    static class MyMethodVisitor extends AdviceAdapter{

        private String className;
        private String methodName;

        private Label startScope = new Label();
        private Label endScope = new Label();
        private Label handleCode = new Label();

        public MyMethodVisitor(String className, String methodName,
                               int api, MethodVisitor methodVisitor, int access,
                               String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor);
            this.className = className;
            this.methodName = methodName;
        }

        @Override
        protected void onMethodEnter() {
            //try start
            visitLabel(startScope);
            visitTryCatchBlock(startScope, endScope, handleCode, "java/lang/Exception");
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            //try end
            mv.visitLabel(endScope);

            //exception handle
            mv.visitLabel(handleCode);
            mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});

            //save exception info to local
            final int local = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(ASTORE, local);

            //out
            mv.visitLdcInsn(String.format("Class: [%s], Method [%s]", className, methodName));
            mv.visitVarInsn(ALOAD, local);
            //invoke point(String methodName, Throwable throwable)
            mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(MethodToTest.class), "record",
                               "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);

            // 抛出异常
            mv.visitVarInsn(ALOAD, local);
            mv.visitInsn(ATHROW);

            super.visitMaxs(maxStack, maxLocals);
        }

        @Override
        protected void onMethodExit(int opcode) {
            if ((IRETURN <= opcode && opcode <= RETURN) || opcode == ATHROW) {
                int nextLocal = this.nextLocal;
                mv.visitVarInsn(ASTORE, nextLocal);
                mv.visitVarInsn(ALOAD, nextLocal);

                mv.visitLdcInsn(className + "." + methodName);
                mv.visitVarInsn(ALOAD, nextLocal);
                //invoke point(String methodName, Object response)
                mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(MethodToTest.class), "record", "(Ljava/lang/String;Ljava/lang/Object;)V", false);
            }
        }
    }

    public static void record(String methodName, Throwable throwable) {
        System.out.println("Monitor : [name:" + methodName + " exception:" + throwable.getMessage() + "]\r\n");
    }

    public static void record(String methodName, Object response) {
        System.out.println("Monitor : [name:" + methodName + " output:" + response + "]\r\n");
    }


    private static void outputClazz(byte[] bytes) {
        final String path = MethodToTest.class.getResource("/").getPath() + "AsmMethodToTest.class";
        try (FileOutputStream out = new FileOutputStream(path)) {
            System.out.println("ASM output path:" + path);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
