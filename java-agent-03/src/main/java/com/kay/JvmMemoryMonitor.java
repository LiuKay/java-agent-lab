package com.kay;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JvmMemoryMonitor {

    public static void start(){
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(new MetricJob(), 1, 5, TimeUnit.SECONDS);
    }


    static class MetricJob implements Runnable {
        @Override
        public void run() {
            new JvmMemoryMetricsCollector().collect();
        }

    }
}
