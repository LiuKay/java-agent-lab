package com.kay;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

public class ClassLoadingMetrics {

    public void collect(){
        final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

        Map<String, Object> map = new HashMap<>();
        map.put("jvm_loaded_classes_count", classLoadingMXBean.getLoadedClassCount());
        map.put("jvm_unloaded_classes_count", classLoadingMXBean.getUnloadedClassCount());
        map.put("jvm_loaded_classes_total", classLoadingMXBean.getTotalLoadedClassCount());

        System.out.println("jvm classes loading:" + map);
    }

}
