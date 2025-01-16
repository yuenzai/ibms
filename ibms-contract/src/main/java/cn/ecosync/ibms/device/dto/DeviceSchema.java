package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.metrics.IInstrumentValueType;
import cn.ecosync.ibms.metrics.IObservableDoubleMeasurement;
import cn.ecosync.ibms.metrics.IObservableLongMeasurement;
import cn.ecosync.ibms.metrics.IObservableMeasurement;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class DeviceSchema {
    @NotBlank
    private String name;
    private Boolean monotonically;
    private IInstrumentValueType valueType;

    public Boolean getMonotonically() {
        return monotonically != null ? monotonically : Boolean.FALSE;
    }

    public IInstrumentValueType getValueType() {
        return valueType != null ? valueType : IInstrumentValueType.DOUBLE;
    }

    public final IObservableMeasurement toObservableMeasurement(Meter meter) {
        String instrumentName = getName();
        if (getMonotonically()) {
            if (getValueType() == IInstrumentValueType.DOUBLE) {
                ObservableDoubleMeasurement measurement = meter.counterBuilder(instrumentName).ofDoubles().buildObserver();
                return new IObservableDoubleMeasurement(instrumentName, measurement);
            } else {
                ObservableLongMeasurement measurement = meter.counterBuilder(instrumentName).buildObserver();
                return new IObservableLongMeasurement(instrumentName, measurement);
            }
        } else {
            if (getValueType() == IInstrumentValueType.LONG) {
                ObservableLongMeasurement measurement = meter.gaugeBuilder(instrumentName).ofLongs().buildObserver();
                return new IObservableLongMeasurement(instrumentName, measurement);
            } else {
                ObservableDoubleMeasurement measurement = meter.gaugeBuilder(instrumentName).buildObserver();
                return new IObservableDoubleMeasurement(instrumentName, measurement);
            }
        }
    }
}
