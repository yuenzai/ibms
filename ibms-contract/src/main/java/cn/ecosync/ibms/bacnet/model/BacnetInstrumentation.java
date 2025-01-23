package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.device.model.DeviceDataPointLabel;
import cn.ecosync.ibms.metrics.Instrument;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.serde.TypeReference;
import cn.ecosync.iframework.util.CollectionUtils;
import cn.ecosync.iframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.bacnet.dto.BacnetProperty.PROPERTY_PRESENT_VALUE;

public class BacnetInstrumentation implements Instrument {
    private static final Logger log = LoggerFactory.getLogger(BacnetInstrumentation.class);

    private final List<BacnetDataPoint> dataPoints;
    private final JsonSerde jsonSerde;

    public BacnetInstrumentation(List<BacnetDataPoint> dataPoints, JsonSerde jsonSerde) {
        Assert.notEmpty(dataPoints, "dataPoints must not be empty");
        this.dataPoints = dataPoints;
        this.jsonSerde = jsonSerde;
    }

    @Override
    public void collectMetrics(MetricsCollector metricsCollector) {
        if (metricsCollector == null) return;
        dataPoints.stream()
                .collect(Collectors.groupingBy(BacnetDataPoint::getDeviceInstance))
                .forEach((deviceInstance, dataPoints) -> collect(deviceInstance, dataPoints, metricsCollector));
    }

    @Override
    public List<BacnetDataPoint> getDataPoints() {
        return dataPoints;
    }

    private void collect(Integer deviceInstance, List<BacnetDataPoint> dataPoints, MetricsCollector metricsCollector) {
        ReadPropertyMultipleAck ack = scrape(deviceInstance, dataPoints);
        Map<BacnetObject, Map<BacnetProperty, BacnetPropertyResult>> propertiesMap = ack.toMap();
        for (BacnetDataPoint dataPoint : dataPoints) {
            BacnetObjectProperties objectProperties = dataPoint.toBacnetObjectProperties();
            Map<BacnetProperty, BacnetPropertyResult> propertyMap = propertiesMap.get(objectProperties.getBacnetObject());
            BacnetPropertyValue presentValue = Optional.ofNullable(propertyMap.get(PROPERTY_PRESENT_VALUE))
                    .map(BacnetPropertyResult::getValue)
                    .orElse(null);
            if (presentValue == null) continue;
            String metricName = dataPoint.getDataPointId().getMetricName();
            double value = presentValue.getValueAsNumber().doubleValue();
            metricsCollector.collect(metricName, value, new DeviceDataPointLabel("device_instance", deviceInstance.toString()));
        }
    }

    private ReadPropertyMultipleAck scrape(Integer deviceInstance, List<BacnetDataPoint> dataPoints) {
        List<BacnetObjectProperties> objectPropertiesCollection = dataPoints.stream()
                .map(BacnetObjectProperties::new)
                .collect(Collectors.toList());
        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, objectPropertiesCollection);
        return scrape(service);
    }

    private ReadPropertyMultipleAck scrape(BacnetReadPropertyMultipleService service) {
        try {
            return doScrape(service);
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    private ReadPropertyMultipleAck doScrape(BacnetReadPropertyMultipleService service) throws Exception {
        List<String> command = service.toCommand();
        if (CollectionUtils.isEmpty(command)) return ReadPropertyMultipleAck.nullInstance(service.getDeviceInstance());

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        int exitValue = process.exitValue();
        if (exitValue != 0) log.error("{}", stderr);
        log.debug("command: {}, exitValue: {}\nstdout:\n{}\nstderr:\n{}", command, exitValue, stdout, stderr);
        if (StringUtils.hasText(stderr)) {
            throw new RuntimeException("ReadPropertyMultiple occurred error: " + stderr);
        }
        return jsonSerde.deserialize(stdout, new TypeReference<ReadPropertyMultipleAck>() {
        });
    }
}
