package com.kay;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;

public class JvmMemoryMetricsCollector {

    public void collect(){
        collectMemoryAreaMetrics();
        collectMemoryPoolMetrics();
    }

    private void collectMemoryPoolMetrics() {
        final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        System.err.println("========================memory poll===================================================");
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            final String poolName = memoryPoolMXBean.getName();

            JvmMemoryMetricModel metricModel = new JvmMemoryMetricModel(
                    memoryPoolMXBean.getUsage().getInit(),
                    memoryPoolMXBean.getUsage().getUsed(),
                    memoryPoolMXBean.getUsage().getCommitted(),
                    memoryPoolMXBean.getUsage().getMax()
            );
            System.out.println(poolName + ":" + metricModel);
        }
    }

    private void collectMemoryAreaMetrics(){
        final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        final MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        final MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        JvmMemoryMetricModel heapMetrics = new JvmMemoryMetricModel(
                heapMemoryUsage.getInit(),
                heapMemoryUsage.getUsed(),
                heapMemoryUsage.getCommitted(),
                heapMemoryUsage.getMax()
        );

        System.out.println("-------------heap------------------");
        System.out.println(heapMetrics);


        JvmMemoryMetricModel noHeapMetrics = new JvmMemoryMetricModel(
                nonHeapMemoryUsage.getInit(),
                nonHeapMemoryUsage.getUsed(),
                nonHeapMemoryUsage.getCommitted(),
                nonHeapMemoryUsage.getMax()
        );

        System.out.println("------------no-heap-------------------");
        System.out.println(noHeapMetrics);
    }

}
