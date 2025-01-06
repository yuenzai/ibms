package cn.ecosync.ibms.metrics;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.ObservableMeasurement;

public interface Measurement {
    void record(ObservableMeasurement observableMeasurement, Attributes attributes);
}
