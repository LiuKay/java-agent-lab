package com.kay;

import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.IOException;

/**
 * Export JVM metrics to prometheus
 *
 * io.prometheus.client.hotspot.DefaultExports include all the jvm information needed.
 */
public final class JvmMemoryMonitor {

    private JvmMemoryMonitor() {}

    public static void start(){
        DefaultExports.initialize(); // jvm exporters
        try {
            new HTTPServer(18888);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
