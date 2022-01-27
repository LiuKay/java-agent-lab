package com.kay.bytebuddy;

import net.bytebuddy.asm.Advice;

public class HelloRebootAdvice {

    @Advice.OnMethodEnter
    public static long enter(@Advice.This Object thisObject,
                             @Advice.Origin String origin,
                             @Advice.Origin("#t #m") String detailedOriginal,
                             @Advice.AllArguments Object[] ary,
                             @Advice.FieldValue(value = "name", readOnly = false) String nameField){

        System.out.println(">>enter method");

        if(ary != null) {
            for(int i =0 ; i < ary.length ; i++){
                System.out.println("Argument: " + i + " is " + ary[i]);
            }
        }

        System.out.println("Origin :" + origin);
        System.out.println("Detailed Origin :" + detailedOriginal);

        nameField = "Kay";
        return System.nanoTime();
    }

    @Advice.OnMethodExit
    public static void exit(@Advice.Enter long time){
        System.out.println(">>exit method");
        System.out.println("Method execution time: " + (System.nanoTime()-time) + "ns");
    }

}
