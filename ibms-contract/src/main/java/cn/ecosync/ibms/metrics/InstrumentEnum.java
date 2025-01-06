package cn.ecosync.ibms.metrics;

import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableMeasurement;

public enum InstrumentEnum {
    LONG_COUNTER,
    DOUBLE_COUNTER,
    LONG_UP_DOWN_COUNTER,
    DOUBLE_UP_DOWN_COUNTER,
    LONG_GAUGE,
    DOUBLE_GAUGE,
    LONG_HISTOGRAM,
    DOUBLE_HISTOGRAM,
    ;

    public ObservableMeasurement toObservableMeasurement(Meter meter, String instrumentName) {
        switch (this) {
            case LONG_COUNTER:
                return meter.counterBuilder(instrumentName).buildObserver();
            case DOUBLE_COUNTER:
                return meter.counterBuilder(instrumentName).ofDoubles().buildObserver();
            case LONG_UP_DOWN_COUNTER:
                return meter.upDownCounterBuilder(instrumentName).buildObserver();
            case DOUBLE_UP_DOWN_COUNTER:
                return meter.upDownCounterBuilder(instrumentName).ofDoubles().buildObserver();
            case LONG_GAUGE:
                return meter.gaugeBuilder(instrumentName).ofLongs().buildObserver();
            case DOUBLE_GAUGE:
                return meter.gaugeBuilder(instrumentName).buildObserver();
            default:
                return null;
        }
    }

    public static InstrumentEnum getInstrumentEnum(InstrumentKindEnum instrumentKind, MeasurementTypeEnum measurementType) {
        return InstrumentEnum.values()[instrumentKind.i + measurementType.i];
    }
}
