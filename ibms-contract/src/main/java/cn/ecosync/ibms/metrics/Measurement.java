package cn.ecosync.ibms.metrics;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import io.opentelemetry.api.metrics.ObservableMeasurement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Measurement {
    private final String instrumentName;
    private final ObservableMeasurement observableMeasurement;
    private final InstrumentKindEnum instrumentKind;
    private final MeasurementTypeEnum measurementType;

    public void record(Number number, Attributes attributes) {
        switch (measurementType) {
            case LONG:
                ObservableLongMeasurement longMeasurement = (ObservableLongMeasurement) observableMeasurement;
                longMeasurement.record(number.longValue(), attributes);
                break;
            case DOUBLE:
                ObservableDoubleMeasurement doubleMeasurement = (ObservableDoubleMeasurement) observableMeasurement;
                doubleMeasurement.record(number.doubleValue(), attributes);
                break;
        }
    }
}
