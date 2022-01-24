package com.kay;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {

    public static final String CLASSNAME = "com.kay.TargetClass";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws
            IllegalClassFormatException {

        if (!className.replaceAll("/", ".").equals(CLASSNAME)) {
            return classfileBuffer;
        }

        try {
            CtClass ctClass = ClassPool.getDefault().get(CLASSNAME);
            final CtMethod method = ctClass.getDeclaredMethod("say");
            method.insertBefore("{ System.out.println(\">>method begin\"); \n}");
            method.insertAfter("{ System.out.println(\"<<method end\"); \n}");

            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
