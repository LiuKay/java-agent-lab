package com.kay.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestMain {

    private static final int _1MB = 1024 * 1024;

    /**
     * -Xms20M -Xmx20M -Xmn10M
     * -javaagent:C:\kaybee\code\bytecode-lab\java-agent-04\build\libs\java-agent-04.jar
     */
    public static void main(String[] args) {
        run();
    }


    public static void run(){
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(()->{
            System.err.println("run...");

            byte[] bytes1, bytes2, bytes3;
            bytes1 = new byte[_1MB * 2];
            bytes2 = new byte[_1MB * 2];
            bytes3 = new byte[_1MB * 2];


        }, 0, 3, TimeUnit.SECONDS);
    }
}
