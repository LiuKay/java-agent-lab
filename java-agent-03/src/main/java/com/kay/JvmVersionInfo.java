package com.kay;

import java.util.HashMap;
import java.util.Map;

public class JvmVersionInfo {

    private static final String UNKNOWN = "unknown";

    public void collect() {
        Map<String, String> infos = new HashMap<>();
        infos.put("version", System.getProperty("java.runtime.version", UNKNOWN));
        infos.put("vendor", System.getProperty("java.vm.vendor", UNKNOWN));
        infos.put("runtime", System.getProperty("java.runtime.name", UNKNOWN));

        System.out.println("jvm info:" + infos);
    }

    public static void main(String[] args) {
        new JvmVersionInfo().collect();
    }

}
