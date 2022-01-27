package com.kay.bytebuddy;

import net.bytebuddy.asm.Advice;

public class UserServiceAdvice {

    @Advice.OnMethodEnter
    public static void enter(){
        System.out.println("hello!");
    }

    @Advice.OnMethodExit
    public static void exit(){
        System.out.println("end!");
    }

}
