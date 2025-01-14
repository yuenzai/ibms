package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import cn.ecosync.ibms.device.dto.DeviceSchema;
import cn.ecosync.ibms.metrics.IObservableDoubleMeasurement;
import cn.ecosync.ibms.metrics.IObservableLongMeasurement;
import cn.ecosync.ibms.metrics.IObservableMeasurement;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import io.opentelemetry.sdk.metrics.InstrumentValueType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetDataAcquisition.class, name = "BACNET"))
public abstract class DeviceDataAcquisition implements IDeviceDataAcquisition {
    @Valid
    @NotNull
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;

    protected DeviceDataAcquisition() {
    }

    protected DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        this.dataAcquisitionId = dataAcquisitionId;
    }

    @Override
    public abstract DeviceSchemas getSchemas();

    @Override
    public abstract List<? extends Device> getDevices();

    public abstract DeviceDataAcquisition addDeviceReferences(List<Device> devices);

    public abstract DeviceDataAcquisition removeDeviceReferences(List<Device> devices);

    public abstract DeviceDataAcquisition withSchemas(DeviceSchemas schemas);

    public abstract DeviceDataAcquisition withDevices(List<? extends Device> devices);

    public abstract DeviceDataAcquisition toReference();

    public final Map<String, IObservableMeasurement> toObservableMeasurements(Meter meter) {
        Collection<DeviceId> deviceIds = getDevices().stream()
                .filter(Objects::nonNull)
                .map(Device::getDeviceId)
                .collect(Collectors.toList());
        Collection<? extends DeviceSchema> schemas = getSchemas().getSchemas();
        Map<String, IObservableMeasurement> observableMeasurements = new LinkedHashMap<>();

        for (DeviceId deviceId : deviceIds) {
            for (DeviceSchema schema : schemas) {
                String instrumentName = deviceId.toString() + "." + schema.getName();

                if (schema.getMonotonically()) {
                    if (schema.getValueType() == InstrumentValueType.DOUBLE) {
                        ObservableDoubleMeasurement measurement = meter.counterBuilder(instrumentName).ofDoubles().buildObserver();
                        observableMeasurements.put(instrumentName, new IObservableDoubleMeasurement(instrumentName, measurement));
                    } else {
                        ObservableLongMeasurement measurement = meter.counterBuilder(instrumentName).buildObserver();
                        observableMeasurements.put(instrumentName, new IObservableLongMeasurement(instrumentName, measurement));
                    }
                } else {
                    if (schema.getValueType() == InstrumentValueType.LONG) {
                        ObservableLongMeasurement measurement = meter.gaugeBuilder(instrumentName).ofLongs().buildObserver();
                        observableMeasurements.put(instrumentName, new IObservableLongMeasurement(instrumentName, measurement));
                    } else {
                        ObservableDoubleMeasurement measurement = meter.gaugeBuilder(instrumentName).buildObserver();
                        observableMeasurements.put(instrumentName, new IObservableDoubleMeasurement(instrumentName, measurement));
                    }
                }
            }
        }
        return observableMeasurements;
    }
}
