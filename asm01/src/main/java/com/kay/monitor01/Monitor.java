package com.kay.monitor01;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class Monitor extends ClassLoader {

    public static void main(String[] args) throws Exception {
        final ClassReader classReader = new ClassReader(MethodToBeMonitor.class.getName());
        final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        final ClassVisitor classVisitor = new MonitorClassVisitor(classWriter, MethodToBeMonitor.class.getSimpleName());
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

        final byte[] bytes = classWriter.toByteArray();
        outputClazz(bytes);

        final Class<?> clazz = new Monitor().defineClass("com.kay.monitor.MethodToBeMonitor", bytes, 0, bytes.length);
        final Method method = clazz.getMethod("sum", int.class, int.class);
        final Object result = method.invoke(clazz.getDeclaredConstructor().newInstance(), 1, 2);

        System.out.println("method result=" + result);

    }

    static class MonitorClassVisitor extends ClassVisitor {
        private String className;

        public MonitorClassVisitor(ClassVisitor classVisitor, String innerClassName) {
            super(Opcodes.ASM6, classVisitor);
            this.className = innerClassName;
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            System.out.println(String.format("Visit (%s,%s,%s,%s)", className, access, name, desc));

            if (!"sum".equals(name)) {
                return super.visitMethod(access, name, desc, signature, exceptions);
            }

            final MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
            return new MonitorClassMethodVisitor(methodVisitor, access, name, desc);
        }

    }

    static class MonitorClassMethodVisitor extends AdviceAdapter {

        private String methodName;

        protected MonitorClassMethodVisitor(MethodVisitor methodVisitor, int access, String name,
                                            String descriptor) {
            super(Opcodes.ASM6, methodVisitor, access, name, descriptor);
            this.methodName = name;
        }

        @Override
        protected void onMethodEnter() {
            mv.visitLdcInsn(methodName);
            mv.visitInsn(ICONST_2);
            mv.visitIntInsn(NEWARRAY, T_INT);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(IASTORE);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_1);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitInsn(IASTORE);
            mv.visitMethodInsn(INVOKESTATIC, "com/kay/monitor01/MonitorLog", "info", "(Ljava/lang/String;[I)V", false);
        }
    }

    private static void outputClazz(byte[] bytes) {
        final String path = Monitor.class.getResource("/").getPath() + "AsmMethodToBeMonitor.class";
        try (FileOutputStream out = new FileOutputStream(path)) {
            System.out.println("ASM output path:" + path);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
