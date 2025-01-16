package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.dto.DeviceSchema;
import cn.ecosync.ibms.metrics.IObservableMeasurement;
import cn.ecosync.iframework.util.CollectionUtils;
import io.opentelemetry.api.metrics.Meter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public interface IDeviceSchemas {
    Collection<? extends DeviceSchema> getSchemas();

    default boolean checkUniqueName() {
        return CollectionUtils.hasUniqueElement(getSchemas(), DeviceSchema::getName);
    }

    default Map<String, IObservableMeasurement> toObservableMeasurements(Meter meter) {
        Map<String, IObservableMeasurement> observableMeasurements = new LinkedHashMap<>();
        for (DeviceSchema schema : getSchemas()) {
            String instrumentName = schema.getName();
            IObservableMeasurement observableMeasurement = schema.toObservableMeasurement(meter);
            observableMeasurements.put(instrumentName, observableMeasurement);
        }
        return observableMeasurements;
    }
}
