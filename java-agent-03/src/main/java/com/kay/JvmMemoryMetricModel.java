package com.kay;

import java.util.StringJoiner;

public class JvmMemoryMetricModel {
    private Long byesInit;
    private Long bytesUsed;
    private Long bytesCommitted;
    private Long bytesMax;

    public JvmMemoryMetricModel(Long byesInit, Long bytesUsed, Long bytesCommitted, Long bytesMax) {
        this.byesInit = byesInit;
        this.bytesUsed = bytesUsed;
        this.bytesCommitted = bytesCommitted;
        this.bytesMax = bytesMax;
    }

    public Long getByesInit() {
        return byesInit;
    }

    public void setByesInit(Long byesInit) {
        this.byesInit = byesInit;
    }

    public Long getBytesUsed() {
        return bytesUsed;
    }

    public void setBytesUsed(Long bytesUsed) {
        this.bytesUsed = bytesUsed;
    }

    public Long getBytesCommitted() {
        return bytesCommitted;
    }

    public void setBytesCommitted(Long bytesCommitted) {
        this.bytesCommitted = bytesCommitted;
    }

    public Long getBytesMax() {
        return bytesMax;
    }

    public void setBytesMax(Long bytesMax) {
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
