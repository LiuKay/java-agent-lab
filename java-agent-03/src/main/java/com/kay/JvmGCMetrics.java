package com.kay;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class JvmGCMetrics {

    private static final Double MISS_PER_SECONDS = 1E3;

    public static void main(String[] args) {
        new JvmGCMetrics().collect();
    }

    public void collect(){
        Map<String, GCMetrics> map = new HashMap<>();
        final List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            map.put(garbageCollectorMXBean.getName(), new GCMetrics(garbageCollectorMXBean.getCollectionCount(),
                                                                    garbageCollectorMXBean.getCollectionTime()/MISS_PER_SECONDS));
        }

        System.out.println("jvm gc:" + map);
    }

    static class GCMetrics {
        final Long collectionCount;
        final Double collectionTimeInSeconds;

        public GCMetrics(Long collectionCount, Double collectionTimeInSeconds) {
            this.collectionCount = collectionCount;
            this.collectionTimeInSeconds = collectionTimeInSeconds;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "[", "]")
                    .add("collectionCount=" + collectionCount)
                    .add("collectionTimeInSeconds=" + collectionTimeInSeconds)
                    .toString();
        }

    }

}
