package com.kay.monitor00;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

/***
 * package com.kay;
 *
 * public class UserService {
 *     public UserService() {
 *     }
 *
 *     public String getUserInfo(String id) {
 *         long var2 = System.nanoTime(); // generate
 *
 *         System.out.println("get user info");
 *         System.out.println("get user info");
 *         System.out.println("get user info");
 *
 *         System.out.println("Method [getUserInfo] time cost:" + (System.nanoTime() - var2) + "(ns)"); // generate
 *         return id;
 *     }
 * }
 */
public class TimeMonitor extends ClassLoader {

    public static void main(String[] args) throws Exception {

        final ClassReader classReader = new ClassReader(UserService.class.getName());
        final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        {
            final MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null,
                                                                        null);
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(Opcodes.RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }

        final ClassVisitor classVisitor = new ProfilingClassAdapter(classWriter, UserService.class.getSimpleName());
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

        final byte[] bytes = classWriter.toByteArray();
        outputClazz(bytes);

        final Class<?> clazz = new TimeMonitor().defineClass("com.kay.timemonitor.UserService", bytes, 0, bytes.length);
        final Method getUserInfo = clazz.getMethod("getUserInfo", String.class);
        final Object result = getUserInfo.invoke(clazz.getDeclaredConstructor().newInstance(), "10086");

        System.out.println("get user info:" + result);

    }

    static class ProfilingClassAdapter extends ClassVisitor {
        private String className;

        public ProfilingClassAdapter(ClassVisitor classVisitor, String innerClassName) {
            super(Opcodes.ASM6, classVisitor);
            this.className = innerClassName;
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

            System.out.println(String.format("Visit (%s,%s,%s,%s)", className, access, name, desc));

            if (!"getUserInfo".equals(name)) {
                return null;
            }

            final MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
            return new ProfilingMethodVisitor(methodVisitor, access, name, desc);
        }

    }

    static class ProfilingMethodVisitor extends AdviceAdapter {

        private String methodName;

        protected ProfilingMethodVisitor(MethodVisitor methodVisitor, int access, String name,
                                         String descriptor) {
            super(Opcodes.ASM6, methodVisitor, access, name, descriptor);
            this.methodName = name;
        }

        // long var2 = System.nanoTime();
        @Override
        protected void onMethodEnter() {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
            mv.visitVarInsn(Opcodes.LSTORE, 2);
            mv.visitVarInsn(ALOAD, 1);
        }

        // System.out.println("Method [getUserInfo] time cost:" + (System.nanoTime() - var2) + "(ns)");
        @Override
        protected void onMethodExit(int opcode) {
            // if return or throw exception
            if ((IRETURN <= opcode && opcode <= RETURN) || opcode == ATHROW) {
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                mv.visitInsn(DUP);

                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                mv.visitLdcInsn(String.format("Method [%s] time cost:", methodName));
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                                   "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                mv.visitVarInsn(LLOAD, 2);
                mv.visitInsn(LSUB);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;",
                                   false);

                mv.visitLdcInsn("(ns)");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                                   "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);

                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
        }
    }

    private static void outputClazz(byte[] bytes) {
        final String path = TimeMonitor.class.getResource("/").getPath() + "AsmUserService.class";
        try (FileOutputStream out = new FileOutputStream(path)) {
            System.out.println("ASM output path:" + path);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
