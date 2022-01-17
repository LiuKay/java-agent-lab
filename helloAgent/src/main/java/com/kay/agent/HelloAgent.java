package com.kay.agent;

import java.lang.instrument.Instrumentation;

/**
 * Add manifest using gradle jar plugin
 */
public class HelloAgent {

    //first try
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println(
                "Hello from agent: " + agentArgs + ". current:" + HelloAgent.class.getClassLoader().getClass()
                                                                                  .getName());
    }

    //second try
    public static void premain(String agentArgs) {

    }

}
