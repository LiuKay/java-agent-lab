package com.kay;

public class Main {

    public static void premain(String agentArgs) {
        JvmMemoryMonitor.start();
    }

    public static void agentmain(String args){
        JvmMemoryMonitor.start();
    }

}
