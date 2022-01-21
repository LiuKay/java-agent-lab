package com.kay.transform;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

public class MethodTransformDemo {

    /**
     * compare to @link{com.kay.monitor00.TimeMonitor}
     */
    static class AddTimerAdapter extends ClassVisitor {
        private String owner;
        private boolean isInterface;

        public AddTimerAdapter(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName,
                          String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            owner = name;
            isInterface = (access & ACC_INTERFACE) != 0;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                         String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (!isInterface && mv != null && name.equals("<init>")) {
                mv = new MethodTimerAdapter(mv);
            }

            return mv;
        }

        @Override
        public void visitEnd() {
            if (!isInterface) {
                //add a field timer
                final FieldVisitor fieldVisitor = cv.visitField(ACC_PUBLIC + ACC_STATIC, "timer", "J", null, null);
                if (fieldVisitor != null) {
                    fieldVisitor.visitEnd();
                }
            }
            cv.visitEnd();
        }


        class MethodTimerAdapter extends MethodVisitor {

            public MethodTimerAdapter(MethodVisitor methodVisitor) {
                super(Opcodes.ASM6, methodVisitor);
            }

            //start of the method
            @Override
            public void visitCode() {
                mv.visitCode();
                mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J",false);
                mv.visitInsn(LSUB);
                mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
            }

            @Override
            public void visitInsn(int opcode) {
                //if method return or throw exception
                if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                    mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J",false);
                    mv.visitInsn(LADD);
                    mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
                }
                super.visitInsn(opcode);
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                super.visitMaxs(maxStack + 4, maxLocals);
            }
        }
    }


}
