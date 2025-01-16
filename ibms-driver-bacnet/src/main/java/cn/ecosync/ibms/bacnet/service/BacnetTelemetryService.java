package cn.ecosync.ibms.bacnet.service;

import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import cn.ecosync.ibms.bacnet.model.BacnetDevice;
import cn.ecosync.ibms.bacnet.model.BacnetSchema;
import cn.ecosync.ibms.bacnet.model.BacnetSchemas;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.metrics.IObservableMeasurement;
import cn.ecosync.ibms.metrics.TelemetryService;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.serde.TypeReference;
import cn.ecosync.iframework.util.CollectionUtils;
import cn.ecosync.iframework.util.StringUtils;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.ObservableMeasurement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class BacnetTelemetryService implements TelemetryService {
    private static final AttributeKey<String> DEVICE_ID = AttributeKey.stringKey("device.id");
    private static final AttributeKey<String> DEVICE_SCHEMAS_ID = AttributeKey.stringKey("device.schemas.id");

    private final BacnetDataAcquisition bacnetDataAcquisition;
    private final JsonSerde jsonSerde;
    private final Map<String, IObservableMeasurement> observableMeasurements;

    @Override
    public void telemetry() {
        try {
            log.info("telemetry...");
            doTelemetry();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void doTelemetry() throws Exception {
        List<BacnetDevice> devices = bacnetDataAcquisition.getDevices();
        Map<Integer, BacnetDevice> deviceMap = CollectionUtils.newHashMap(devices.size());
        List<BacnetReadPropertyMultipleService> services = new ArrayList<>(devices.size());
        for (BacnetDevice device : devices) {
            Integer deviceInstance = device.getDeviceInstance();
            if (deviceInstance == null) continue;
            deviceMap.put(deviceInstance, device);
            BacnetReadPropertyMultipleService service = BacnetReadPropertyMultipleService.newInstance(deviceInstance, bacnetDataAcquisition.getSchemas());
            services.add(service);
        }

        List<ReadPropertyMultipleAck> acks = execute(services);

        for (ReadPropertyMultipleAck ack : acks) {
            Integer deviceInstance = ack.getDeviceInstance();
            BacnetDevice device = deviceMap.get(deviceInstance);
            record(device, ack);
        }
    }

    private void record(BacnetDevice device, ReadPropertyMultipleAck ack) {
        BacnetSchemas bacnetSchemas = bacnetDataAcquisition.getSchemas();
        List<BacnetSchema> schemas = bacnetSchemas.getSchemas();
        Map<BacnetObject, Map<BacnetProperty, BacnetPropertyResult>> propertiesMap = ack.toMap();

        for (BacnetSchema schema : schemas) {
            BacnetObjectProperties objectProperties = schema.getSchemaProperties();
            Map<BacnetProperty, BacnetPropertyResult> propertyMap = propertiesMap.get(objectProperties.getBacnetObject());
            BacnetPropertyValue presentValue = Optional.ofNullable(propertyMap.get(BacnetProperty.PROPERTY_PRESENT_VALUE))
                    .map(BacnetPropertyResult::getValue)
                    .orElse(null);
            if (presentValue == null) continue;

            DeviceId deviceId = device.getDeviceId();
            IObservableMeasurement measurement = observableMeasurements.get(schema.getName());
            if (measurement == null) continue;

            AttributesBuilder builder = Attributes.builder();
            builder.put(DEVICE_ID, deviceId.toString());
            builder.put(DEVICE_SCHEMAS_ID, bacnetSchemas.getSchemasId().toString());
            propertyMap.values().forEach(in -> in.accept(builder));
            Attributes attributes = builder.build();

            measurement.record(presentValue.getValueAsNumber(), attributes);
        }
    }

    private List<ReadPropertyMultipleAck> execute(List<BacnetReadPropertyMultipleService> services) throws Exception {
        String command = services.stream()
                .filter(Objects::nonNull)
                .map(BacnetReadPropertyMultipleService::toCommandString)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining("; echo; "));

        if (!StringUtils.hasText(command)) return Collections.emptyList();

        List<String> commands = Arrays.asList("/bin/bash", "-c", command);
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        int exitValue = process.exitValue();
        if (exitValue != 0) log.error("{}", stderr);
        log.debug("command: {}, exitValue: {}\nstdout:\n{}\nstderr:\n{}", commands, exitValue, stdout, stderr);

        return Arrays.stream(stdout.split("\n"))
                .filter(StringUtils::hasText)
                .map(in -> jsonSerde.deserialize(in, new TypeReference<ReadPropertyMultipleAck>() {
                }))
                .filter(ReadPropertyMultipleAck::valuesNotEmpty)
                .collect(Collectors.toList());
    }

    @Override
    public Stream<ObservableMeasurement> getMeasurements() {
        return observableMeasurements.values().stream()
                .peek(in -> log.info("instrumentName: {}", in.getInstrumentName()))
                .map(IObservableMeasurement::getObservableMeasurement);
    }
}
