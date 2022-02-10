package com.kay;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class JvmMemoryMonitor {

    public static void start(){
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(new MetricJob(), 1, 5, TimeUnit.SECONDS);
    }


    static class MetricJob implements Runnable {

        private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");

        @Override
        public void run() {

            final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

            for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
                final String name = memoryPoolMXBean.getName();
                final String poolName = WHITESPACE.matcher(name).replaceAll("-");

                JvmMemoryMetricModel metricModel = new JvmMemoryMetricModel(
                        memoryPoolMXBean.getUsage().getInit(),
                        memoryPoolMXBean.getUsage().getUsed(),
                        memoryPoolMXBean.getUsage().getCommitted(),
                        memoryPoolMXBean.getUsage().getMax()
                );

                System.out.println(poolName + ":" + metricModel);

            }
            System.err.println("===========================================================================");
        }

    }
}
