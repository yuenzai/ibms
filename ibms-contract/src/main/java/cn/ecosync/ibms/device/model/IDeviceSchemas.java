package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.dto.DeviceSchema;
import cn.ecosync.ibms.metrics.InstrumentEnum;
import cn.ecosync.ibms.metrics.InstrumentKindEnum;
import cn.ecosync.ibms.metrics.Measurement;
import cn.ecosync.ibms.metrics.MeasurementTypeEnum;
import cn.ecosync.iframework.util.CollectionUtils;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableMeasurement;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public interface IDeviceSchemas {
    Collection<? extends DeviceSchema> getSchemas();

    default Map<String, Measurement> toObservableMeasurements(Meter meter, Collection<DeviceId> deviceIds) {
        Collection<? extends DeviceSchema> schemas = getSchemas();
        Map<String, Measurement> observableMeasurements = new LinkedHashMap<>();

        for (DeviceId deviceId : deviceIds) {
            for (DeviceSchema schema : schemas) {
                InstrumentKindEnum instrumentKind = schema.getInstrumentKind();
                MeasurementTypeEnum measurementType = schema.getMeasurementType();
                String instrumentName = deviceId.toString() + "." + schema.getName();
                Measurement measurement;

                ObservableMeasurement observableMeasurement = InstrumentEnum.getInstrumentEnum(instrumentKind, measurementType)
                        .toObservableMeasurement(meter, instrumentName);
                if (observableMeasurement != null) {
                    measurement = new Measurement(instrumentName, observableMeasurement, instrumentKind, measurementType);
                    observableMeasurements.put(instrumentName, measurement);
                }
            }
        }
        return observableMeasurements;
    }

    default boolean checkUniqueName() {
        return CollectionUtils.hasUniqueElement(getSchemas(), DeviceSchema::getName);
    }
}
