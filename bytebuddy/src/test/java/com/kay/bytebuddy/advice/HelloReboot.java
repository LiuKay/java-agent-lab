package com.kay.bytebuddy.advice;

public class HelloReboot {

    private String name = "fixedName";

    public String sayHello(String name){
        return "hello, " + name;
    }
}
