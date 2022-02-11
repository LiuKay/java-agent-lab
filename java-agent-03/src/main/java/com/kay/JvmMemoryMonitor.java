package com.kay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            System.err.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            new MemoryMetrics().collect();
            new ThreadMetrics().collect();
            new JvmVersionInfo().collect();
            new ClassLoadingMetrics().collect();
            new JvmGCMetrics().collect();
        }

    }
}
