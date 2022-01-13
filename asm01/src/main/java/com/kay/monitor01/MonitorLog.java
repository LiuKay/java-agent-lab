package com.kay.monitor01;

public class MonitorLog {

    public static void info(String name, int... parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object parameter : parameters) {
            stringBuilder.append(parameter).append(",");
        }
        System.out.println(String.format("Method:[%s], Args:[%s]", name, stringBuilder));
    }


}
