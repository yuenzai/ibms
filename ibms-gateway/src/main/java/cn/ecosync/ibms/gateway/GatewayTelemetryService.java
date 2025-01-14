package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import cn.ecosync.ibms.bacnet.service.BacnetTelemetryService;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.metrics.IObservableMeasurement;
import cn.ecosync.iframework.serde.JsonSerde;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.BatchCallback;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableMeasurement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
public class GatewayTelemetryService implements GatewayRepository {
    private final OpenTelemetry openTelemetry;
    private final JsonSerde jsonSerde;
    private final AtomicReference<DeviceGateway> gatewayRef = new AtomicReference<>();
    private final Collection<BatchCallback> callbacks = Collections.synchronizedCollection(new ArrayList<>());

    @Override
    public DeviceGateway saveAndGet(DeviceGateway gateway) {
        log.info("同步网关配置成功: {}", gateway);
        return gatewayRef.updateAndGet(in -> gateway);
    }

    @Override
    public Optional<DeviceGateway> get() {
        return Optional.ofNullable(gatewayRef.get());
    }

    public void observeMeasurements(DeviceGateway gateway) {
        callbacks.forEach(BatchCallback::close);
        callbacks.clear();
        if (gateway == null) return;
        gateway.getDataAcquisitions().stream()
                .map(this::observeMeasurements)
                .filter(Objects::nonNull)
                .forEach(callbacks::add);
    }

    private BatchCallback observeMeasurements(DeviceDataAcquisition dataAcquisition) {
        Runnable callback;

        Meter meter = openTelemetry.getMeter(dataAcquisition.getSchemas().getSchemasId().toString());
        Map<String, IObservableMeasurement> iobservableMeasurements = dataAcquisition.toObservableMeasurements(meter);
        if (iobservableMeasurements.isEmpty()) {
            return null;
        } else if (dataAcquisition instanceof BacnetDataAcquisition) {
            BacnetDataAcquisition bacnetDataAcquisition = (BacnetDataAcquisition) dataAcquisition;
            callback = new BacnetTelemetryService(bacnetDataAcquisition, jsonSerde, iobservableMeasurements);
        } else {
            return null;
        }
        ObservableMeasurement[] observableMeasurements = iobservableMeasurements.values().stream()
                .peek(in -> log.info("instrumentName: {}", in.getInstrumentName()))
                .map(IObservableMeasurement::getObservableMeasurement)
                .toArray(ObservableMeasurement[]::new);
        return meter.batchCallback(callback, null, observableMeasurements);
    }
}
