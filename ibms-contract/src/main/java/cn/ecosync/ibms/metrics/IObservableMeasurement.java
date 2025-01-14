package cn.ecosync.ibms.metrics;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.ObservableMeasurement;

public interface IObservableMeasurement {
    /**
     * Records a measurement.
     *
     * @param value The measurement value.
     */
    default void record(Number value) {
        record(value, Attributes.empty());
    }

    /**
     * Records a measurement with a set of attributes.
     *
     * @param value      The measurement value.
     * @param attributes A set of attributes to associate with the value.
     */
    void record(Number value, Attributes attributes);

    String getInstrumentName();

    ObservableMeasurement getObservableMeasurement();
}
