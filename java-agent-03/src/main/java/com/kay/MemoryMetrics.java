package com.kay;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class MemoryMetrics {

    public static void main(String[] args) {
        new MemoryMetrics().collect();
    }

    public void collect(){
        final Map<String, JvmMemoryMetricModel> memoryMetricModelMap = collectMemoryAreaMetrics();
        final Map<String, JvmMemoryMetricModel> memoryPoolMetrics = collectMemoryPoolMetrics();

        System.out.println("memory area:" + memoryMetricModelMap);
        System.out.println("memory pool:" + memoryPoolMetrics);
    }

    private Map<String, JvmMemoryMetricModel> collectMemoryPoolMetrics() {
        final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        Map<String, JvmMemoryMetricModel> memoryMetricModelMap = new HashMap<>();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            final String poolName = memoryPoolMXBean.getName();
            JvmMemoryMetricModel metricModel = new JvmMemoryMetricModel(
                    memoryPoolMXBean.getUsage().getInit(),
                    memoryPoolMXBean.getUsage().getUsed(),
                    memoryPoolMXBean.getUsage().getCommitted(),
                    memoryPoolMXBean.getUsage().getMax()
            );
            memoryMetricModelMap.put(poolName, metricModel);
        }

        return memoryMetricModelMap;
    }

    private Map<String, JvmMemoryMetricModel> collectMemoryAreaMetrics(){
        final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        final MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        final MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        Map<String, JvmMemoryMetricModel> memoryMetricModelMap = new HashMap<>();
        memoryMetricModelMap.put("heap", new JvmMemoryMetricModel(
                heapMemoryUsage.getInit(),
                heapMemoryUsage.getUsed(),
                heapMemoryUsage.getCommitted(),
                heapMemoryUsage.getMax()
        ));
        memoryMetricModelMap.put("no-heap", new JvmMemoryMetricModel(
                nonHeapMemoryUsage.getInit(),
                nonHeapMemoryUsage.getUsed(),
                nonHeapMemoryUsage.getCommitted(),
                nonHeapMemoryUsage.getMax()
        ));

        return memoryMetricModelMap;
    }

    static class JvmMemoryMetricModel {
        final Long byesInit;
        final Long bytesUsed;
        final Long bytesCommitted;
        final Long bytesMax;

        public JvmMemoryMetricModel(Long byesInit, Long bytesUsed, Long bytesCommitted, Long bytesMax) {
            this.byesInit = byesInit;
            this.bytesUsed = bytesUsed;
            this.bytesCommitted = bytesCommitted;
            this.bytesMax = bytesMax;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "[", "]")
                    .add("byesInit=" + byesInit)
                    .add("bytesUsed=" + bytesUsed)
                    .add("bytesCommitted=" + bytesCommitted)
                    .add("bytesMax=" + bytesMax)
                    .toString();
        }
    }

}
