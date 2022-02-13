package com.kay.example;


import java.lang.instrument.Instrumentation;

public class Bootstrap {

    public static void premain(String args, Instrumentation inst){
        ClassLoader classLoader = Bootstrap.class.getClassLoader();
        System.out.println("Bootstrap:" + classLoader);


        DependencyClass dependencyClass = new DependencyClass();
        dependencyClass.version2();

    }

}
