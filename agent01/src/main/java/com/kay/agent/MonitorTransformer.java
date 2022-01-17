package com.kay.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class MonitorTransformer implements ClassFileTransformer {

    private static final Set<String> classNameSet = new HashSet<>();

    static {
        classNameSet.add("com.kay.agent.AgentTest");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        String currentClassName = className.replaceAll("/", ".");
        if (!classNameSet.contains(currentClassName)) {
            return null;
        }

        System.out.println(String.format("transform class name : %s", currentClassName));

        try {
            CtClass ctClass = ClassPool.getDefault().get(currentClassName);
            CtBehavior[] declaredBehaviors = ctClass.getDeclaredBehaviors();

            for (CtBehavior behavior : declaredBehaviors) {
                enhance(behavior);
            }

            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void enhance(CtBehavior behavior) throws CannotCompileException, ClassNotFoundException {
        if (behavior.isEmpty()) {
            return;
        }

        String name = behavior.getName();
        if ("main".equalsIgnoreCase(name)) {
            return;
        }

        if (!behavior.hasAnnotation(TimeMonitor.class)) {
            return;
        }

        TimeMonitor annotation = (TimeMonitor) behavior.getAnnotation(TimeMonitor.class);
        String note = annotation.note();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{")
                     .append("long start = System.nanoTime();\n") //前置增强: 打入时间戳
                     .append("$_ = $proceed($$);\n")              //调用原有代码，类似于method();($$)表示所有的参数
                     .append("System.out.print(")
                     .append(String.format("\"note:%s, method:[", note))
                     .append(name).append("]\");").append("\n")
                     .append("System.out.println(\" cost:\" +(System.nanoTime() - start)+ \"ns\");") // 后置增强，计算输出方法执行耗时
                     .append("}");

        ExprEditor exprEditor = new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                m.replace(stringBuilder.toString());
            }
        };

        behavior.instrument(exprEditor);
    }

}
