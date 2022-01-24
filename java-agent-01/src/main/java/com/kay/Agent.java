package com.kay;

import java.lang.instrument.Instrumentation;

public class Agent {

    //when agent loaded into JVM
    public static void agentmain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new com.kay.MyTransformer(), true);
        try {
            final Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
            for (Class clazz : allLoadedClasses) {
                if (clazz.getName().equals("com.kay.TargetClass")) {
                    System.out.println("Found target class: " + clazz);
                    instrumentation.retransformClasses(clazz);
                }
            }

            System.out.println("Agent loaded.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Agent load failed.");
        }
    }

    // before main
    public static void premain(String agentArgs, Instrumentation instrumentation){
        instrumentation.addTransformer(new com.kay.MyTransformer(), true);
    }

}
