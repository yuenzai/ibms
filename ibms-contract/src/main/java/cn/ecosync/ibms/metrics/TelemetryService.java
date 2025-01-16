package cn.ecosync.ibms.metrics;

import io.opentelemetry.api.metrics.ObservableMeasurement;

import java.util.stream.Stream;

public interface TelemetryService extends Runnable {
    Stream<ObservableMeasurement> getMeasurements();

    @Override
    default void run() {
        telemetry();
    }

    void telemetry();
}
