package cn.ecosync.aiot.edge.gateway.model;

import io.prometheus.metrics.model.snapshots.MetricSnapshot;

import java.util.function.Consumer;

public interface DeviceMetricsCollector {
    void collect(Consumer<MetricSnapshot> metricsConsumer);
}
