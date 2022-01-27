package com.kay.agent.monitor.agent;

import net.bytebuddy.asm.Advice;

import java.util.StringJoiner;

public class MethodAdvice {

    @Advice.OnMethodEnter
    public static long enter(@Advice.Origin String origin,
                             @Advice.Origin("#d") String descriptor,
                             @Advice.AllArguments Object[] args) {
        StringBuilder stringBuilder = new StringBuilder("Args:[");
        StringJoiner joiner = new StringJoiner(",");
        if (args != null) {
            for (Object arg : args) {
                joiner.add(arg == null ? "null" : arg.toString());
            }
        }
        stringBuilder.append(joiner).append("]");

        System.out.println(">>Method:" + origin + "," + stringBuilder);

        return System.nanoTime();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Enter long startTime,
                            @Advice.Thrown Throwable throwable,
                            @Advice.Return Object returnValue) {
        System.out.println(
                String.format("<<Method execution time:%dns, return=%s, exception=%s", System.nanoTime() - startTime,
                              returnValue, throwable));
    }

}
