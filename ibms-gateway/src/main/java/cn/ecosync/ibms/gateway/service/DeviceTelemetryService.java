package cn.ecosync.ibms.gateway.service;

import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.gateway.bacnet.BacnetDeviceMetricsCollector;
import cn.ecosync.ibms.gateway.bacnet.BacnetService;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceInfos;
import cn.ecosync.ibms.gateway.model.DeviceMetricsCollector;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.StringUtils;
import io.prometheus.metrics.model.registry.MultiCollector;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.prometheus.metrics.model.registry.PrometheusScrapeRequest;
import io.prometheus.metrics.model.snapshots.Labels;
import io.prometheus.metrics.model.snapshots.MetricSnapshots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DeviceTelemetryService implements MultiCollector {
    private static final Logger log = LoggerFactory.getLogger(DeviceTelemetryService.class);

    private final PrometheusRegistry prometheusRegistry;
    private final BacnetService bacnetService;
    private final AtomicReference<Map<String, DeviceMetricsCollector>> instrumentsRef = new AtomicReference<>(new HashMap<>());

    public DeviceTelemetryService(PrometheusRegistry prometheusRegistry, BacnetService bacnetService) {
        this.prometheusRegistry = prometheusRegistry;
        this.bacnetService = bacnetService;
    }

    public void reload(DeviceDataAcquisition... dataAcquisitions) {
        log.atInfo().log("重新加载配置");
        prometheusRegistry.clear();
        prometheusRegistry.register(this);
        Map<String, DeviceMetricsCollector> instruments = new HashMap<>();
        for (DeviceDataAcquisition dataAcquisition : dataAcquisitions) {
            switch (dataAcquisition.getDataAcquisitionType()) {
                case BACNET:
                    newBacnetDeviceMetricsCollector(dataAcquisition, instruments::put);
                    break;
                case MODBUS:
                    // no op
                    break;
            }
        }
        instrumentsRef.set(instruments);
    }

    @Override
    public MetricSnapshots collect(PrometheusScrapeRequest scrapeRequest) {
        String deviceCode = CollectionUtils.firstElement(Arrays.asList(scrapeRequest.getParameterValues("target")));
        MDC.put("deviceCode", deviceCode);
        log.atInfo().addKeyValue("requestPath", scrapeRequest.getRequestPath()).addKeyValue("target", deviceCode).log("collect");
        if (!StringUtils.hasText(deviceCode)) return collect();
        Map<String, DeviceMetricsCollector> instruments = instrumentsRef.get();
        MetricSnapshots.Builder metricsBuilder = MetricSnapshots.builder();
        Optional.of(deviceCode)
                .filter(StringUtils::hasText)
                .map(instruments::get)
                .ifPresent(in -> in.collect(metricsBuilder::metricSnapshot));
        MetricSnapshots metricSnapshots = metricsBuilder.build();
        log.atInfo().addKeyValue("sampleCount", metricSnapshots.size()).log("");
        MDC.remove("deviceCode");
        return metricSnapshots;
    }

    @Override
    public MetricSnapshots collect() {
        return new MetricSnapshots();
    }

    private void newBacnetDeviceMetricsCollector(DeviceDataAcquisition dataAcquisition, BiConsumer<String, DeviceMetricsCollector> consumer) {
        DeviceInfos deviceInfos = new DeviceInfos(dataAcquisition.getDeviceInfos());
        Map<String, List<BacnetDataPoint>> deviceDataPointsMap = dataAcquisition.getDataPoints()
                .toLabels()
                .stream()
                .map(BacnetDataPoint::new)
                .collect(Collectors.groupingBy(in -> in.getDataPointId().getDeviceCode()));
        for (Map.Entry<String, List<BacnetDataPoint>> entry : deviceDataPointsMap.entrySet()) {
            String deviceCode = entry.getKey();
            List<BacnetDataPoint> dataPoints = entry.getValue();
            Labels deviceInfo = deviceInfos.get(deviceCode);
            DeviceMetricsCollector deviceMetricsCollector = new BacnetDeviceMetricsCollector(deviceCode, deviceInfo, dataPoints, bacnetService);
            consumer.accept(deviceCode, deviceMetricsCollector);
        }
    }
}
