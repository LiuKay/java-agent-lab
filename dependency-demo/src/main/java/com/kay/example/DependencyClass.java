package com.kay.example;

public class DependencyClass {

    public void version1(){
        System.out.println("version1:" + DependencyClass.class.getClassLoader());
    }


    // This method is only in "com.kay:dependency-demo:2.0"
    //public void version2(){
    //    System.out.println("version2:" + DependencyClass.class.getClassLoader());
    //}


}
