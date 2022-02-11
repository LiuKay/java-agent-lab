package com.kay;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

public class ThreadMetrics {

    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public static void main(String[] args) {
        new ThreadMetrics().collect();
    }

    public void collect(){
        Map<String, Object> threadMetrics = new HashMap<>();
        threadMetrics.put("jvm_threads_count", threadMXBean.getThreadCount());
        threadMetrics.put("jvm_threads_daemon", threadMXBean.getDaemonThreadCount());
        threadMetrics.put("jvm_threads_peak", threadMXBean.getPeakThreadCount());
        threadMetrics.put("jvm_threads_started_total", threadMXBean.getTotalStartedThreadCount());
        threadMetrics.put("jvm_threads_deadlocked", nullSafeLength(threadMXBean.findDeadlockedThreads()));
        threadMetrics.put("jvm_threads_deadlocked_monitor", nullSafeLength(threadMXBean.findMonitorDeadlockedThreads()));

        final Map<Thread.State, Integer> threadStateCountMap = getThreadStateCountMap();
        threadStateCountMap.forEach((state, counter) -> {
            threadMetrics.put(state.toString(), counter);
        });

        System.out.println("jvm threads:" + threadMetrics);
    }

    private static double nullSafeLength(long[] array) {
        return null == array ? 0.0D : (double)array.length;
    }

    private Map<Thread.State, Integer> getThreadStateCountMap() {
        // Get thread information without computing any stack traces
        ThreadInfo[] allThreads = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 0);

        // Initialize the map with all thread states
        HashMap<Thread.State, Integer> threadCounts = new HashMap<>();
        for (Thread.State state : Thread.State.values()) {
            threadCounts.put(state, 0);
        }

        // Collect the actual thread counts
        for (ThreadInfo curThread : allThreads) {
            if (curThread != null) {
                Thread.State threadState = curThread.getThreadState();
                threadCounts.put(threadState, threadCounts.get(threadState) + 1);
            }
        }

        return threadCounts;
    }

}
