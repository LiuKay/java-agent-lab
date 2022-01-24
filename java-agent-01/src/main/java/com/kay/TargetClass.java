package com.kay;

import java.lang.management.ManagementFactory;


public class TargetClass {

    public static void main(String[] args) {
        final TargetClass targetClass = new TargetClass();
        final String name = ManagementFactory.getRuntimeMXBean().getName();
        final String pid = name.split("@")[0];
        System.out.println("pid=" + pid); // process id
        while (true){
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                break;
            }
            targetClass.say();
        }
    }


    public void say(){
        System.out.println("saying...");
    }

}
