package cn.ecosync.ibms.metrics;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement;
import io.opentelemetry.api.metrics.ObservableMeasurement;
import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class IObservableDoubleMeasurement implements IObservableMeasurement {
    private final String instrumentName;
    private final ObservableDoubleMeasurement observableMeasurement;

    public IObservableDoubleMeasurement(String instrumentName, ObservableDoubleMeasurement observableMeasurement) {
        Assert.hasText(instrumentName, "instrumentName must not be null");
        Assert.notNull(observableMeasurement, "observableMeasurement must not be null");
        this.instrumentName = instrumentName;
        this.observableMeasurement = observableMeasurement;
    }

    @Override
    public void record(Number number, Attributes attributes) {
        observableMeasurement.record(number.doubleValue(), attributes);
    }

    @Override
    public String getInstrumentName() {
        return instrumentName;
    }

    @Override
    public ObservableMeasurement getObservableMeasurement() {
        return observableMeasurement;
    }
}
