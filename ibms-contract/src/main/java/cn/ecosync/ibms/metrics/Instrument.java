package cn.ecosync.ibms.metrics;

import cn.ecosync.ibms.device.model.DeviceDataPoint;
import cn.ecosync.ibms.device.model.DeviceDataPointLabel;

import java.util.List;

public interface Instrument {
    void collectMetrics(MetricsCollector metricsCollector);

    List<? extends DeviceDataPoint> getDataPoints();

    interface MetricsCollector {
        void collect(String metricName, double value, DeviceDataPointLabel... labels);
    }
}
