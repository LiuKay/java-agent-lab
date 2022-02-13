package com.kay.example;

public class TestMain {

    //-XX:+TraceClassLoading
    public static void main(String[] args) {

        System.out.println("app:" + TestMain.class.getClassLoader() );

        new DependencyClass().version1();
    }

}
