package com.kay.monitor01;

/**
 * package com.kay.monitor01;
 *
 * public class MethodToBeMonitor {
 *     public MethodToBeMonitor() {
 *     }
 *
 *     public int sum(int a, int b) {
 *         MonitorLog.info("sum", new int[]{a, b});
 *         return a + b;
 *     }
 * }
 */
public class MethodToBeMonitor {

    public int sum(int a, int b) {
        return a + b;
    }

}
