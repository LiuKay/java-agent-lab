package com.kay.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;

import java.lang.reflect.Method;

/**
 * package com.kay.javassist;
 *
 * public class HelloWorld {
 *     public static void main(String[] var0) {
 *         System.out.println("Hello javassist!");
 *     }
 *
 *     public HelloWorld() {
 *     }
 * }
 */
public class Hello {

    public static void main(String[] args) throws Exception {
        final ClassPool pool = ClassPool.getDefault();

        //create class
        final CtClass ctClass = pool.makeClass("com.kay.javassist.HelloWorld");

        //create method "main"
        final CtMethod mainMethod = new CtMethod(CtClass.voidType, "main",
                                                 new CtClass[]{pool.get(String[].class.getName())},
                                                 ctClass);
        mainMethod.setModifiers(Modifier.PUBLIC + Modifier.STATIC);
        mainMethod.setBody("{ System.out.println(\"Hello javassist!\");}");
        ctClass.addMethod(mainMethod);

        //empty constructor
        final CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
        ctConstructor.setBody("{}");

        Utils.outputClass(ctClass.toBytecode(),"HelloWorld");

        final Class<?> clazz = ctClass.toClass();
        final Object instance = clazz.getDeclaredConstructor().newInstance();
        final Method main = clazz.getDeclaredMethod("main", String[].class);
        main.invoke(instance, (Object) new String[1]);
    }

}
