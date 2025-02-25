package cn.ecosync.ibms.gateway.model;

import io.prometheus.metrics.model.snapshots.MetricSnapshot;

import java.util.function.Consumer;

public interface DeviceMetricsCollector {
    void collect(Consumer<MetricSnapshot> metricsConsumer);
}
