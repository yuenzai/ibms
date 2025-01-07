package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import cn.ecosync.ibms.bacnet.service.BacnetTelemetryService;
import cn.ecosync.ibms.device.model.*;
import cn.ecosync.iframework.serde.JsonSerde;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.BatchCallback;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableMeasurement;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GatewayTelemetryService implements GatewayRepository {
    private final OpenTelemetry openTelemetry;
    private final JsonSerde jsonSerde;
    private final AtomicReference<DeviceGateway> gatewayRef = new AtomicReference<>();
    private final Map<DeviceSchemasId, BatchCallback> batchCallbacks = new ConcurrentHashMap<>();

    @Override
    public void save(DeviceGateway gateway) {
        DeviceGateway ref = gatewayRef.updateAndGet(in -> gateway);
        batchCallbacks.values().forEach(BatchCallback::close);
        observeMeasurements(ref);
    }

    @Override
    public Optional<DeviceGateway> get() {
        return Optional.ofNullable(gatewayRef.get());
    }

    private void observeMeasurements(DeviceGateway gateway) {
        if (gateway == null) return;
        gateway.getDataAcquisitions().stream()
                .map(this::observeMeasurements)
                .forEach(this::saveCallback);
    }

    private DeviceSchemasCallback observeMeasurements(DeviceDataAcquisition dataAcquisition) {
        Runnable callback;
        ObservableMeasurement observableMeasurement;
        ObservableMeasurement[] additionalMeasurements;

        DeviceSchemas schemas = dataAcquisition.getSchemas();
        Meter meter = openTelemetry.getMeter(schemas.getSchemasId().toString());
        Collection<DeviceId> deviceIds = dataAcquisition.getDevices().stream()
                .map(Device::getDeviceId)
                .collect(Collectors.toList());
        Map<String, ObservableMeasurement> observableMeasurements = schemas.toObservableMeasurements(meter, deviceIds);
        List<ObservableMeasurement> measurementList = new ArrayList<>(observableMeasurements.values());
        if (measurementList.isEmpty()) return DeviceSchemasCallback.NULL;
        observableMeasurement = measurementList.get(0);
        additionalMeasurements = measurementList.subList(1, measurementList.size())
                .toArray(new ObservableMeasurement[0]);

        if (dataAcquisition instanceof BacnetDataAcquisition) {
            BacnetDataAcquisition bacnetDataAcquisition = (BacnetDataAcquisition) dataAcquisition;
            callback = new BacnetTelemetryService(bacnetDataAcquisition, jsonSerde, observableMeasurements);
        } else {
            return DeviceSchemasCallback.NULL;
        }

        BatchCallback batchCallback = meter.batchCallback(callback, observableMeasurement, additionalMeasurements);
        return new DeviceSchemasCallback(schemas.getSchemasId(), batchCallback);
    }

    private void saveCallback(DeviceSchemasCallback callback) {
        if (callback == DeviceSchemasCallback.NULL) return;
        Optional.ofNullable(batchCallbacks.get(callback.schemasId))
                .ifPresent(BatchCallback::close);
        batchCallbacks.put(callback.schemasId, callback.callback);
    }

    @RequiredArgsConstructor
    private static class DeviceSchemasCallback {
        private static final DeviceSchemasCallback NULL = new DeviceSchemasCallback(null, null);
        private final DeviceSchemasId schemasId;
        private final BatchCallback callback;
    }
}
