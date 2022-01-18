package com.kay.transform;

public class Person {

    private String name;
    private int age;

    public String sayHi(){
        return "hi, my name is :" + name;
    }

    public String sayHi(String name){
        return "hi, my name is :" + name;
    }

    public String getName(){
        return this.name;
    }

    public int getAge(){
        return this.age;
    }

}
