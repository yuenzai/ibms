package cn.ecosync.ibms.gateway.service;

import cn.ecosync.ibms.bacnet.dto.BacnetWhoIsService;
import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.bacnet.model.BacnetDeviceMetricsCollector;
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
    private final AtomicReference<Map<String, DeviceMetricsCollector>> instrumentsRef = new AtomicReference<>(new HashMap<>());

    public DeviceTelemetryService(PrometheusRegistry prometheusRegistry) {
        this.prometheusRegistry = prometheusRegistry;
    }

    public void reload(DeviceDataAcquisition... dataAcquisitions) {
        log.atInfo().log("重新加载配置");
        prometheusRegistry.clear();
        prometheusRegistry.register(this);
        Map<String, DeviceMetricsCollector> instruments = new HashMap<>();
        for (DeviceDataAcquisition dataAcquisition : dataAcquisitions) {
            newDeviceMetricsCollector(dataAcquisition, instruments::put);
        }
        instrumentsRef.set(instruments);

        try {
            BacnetWhoIsService service = new BacnetWhoIsService();
            BacnetWhoIsService.execute(service);
        } catch (Exception e) {
            log.atError().setCause(e).log("");
        }
    }

    @Override
    public MetricSnapshots collect(PrometheusScrapeRequest scrapeRequest) {
        String deviceCode = CollectionUtils.firstElement(Arrays.asList(scrapeRequest.getParameterValues("target")));
        log.atInfo().addKeyValue("requestPath", scrapeRequest.getRequestPath()).addKeyValue("target", deviceCode).log("collect");
        if (!StringUtils.hasText(deviceCode)) return collect();
        Map<String, DeviceMetricsCollector> instruments = instrumentsRef.get();
        MDC.put("deviceCode", deviceCode);
        MetricSnapshots.Builder metricsBuilder = MetricSnapshots.builder();
        Optional.of(deviceCode)
                .filter(StringUtils::hasText)
                .map(instruments::get)
                .ifPresent(in -> in.collect(metricsBuilder::metricSnapshot));
        MetricSnapshots metricSnapshots = metricsBuilder.build();
        log.atInfo().addKeyValue("sampleCount", metricSnapshots.size()).log("采集结束");
        MDC.remove("deviceCode");
        return metricSnapshots;
    }

    @Override
    public MetricSnapshots collect() {
        return new MetricSnapshots();
    }

    private void newDeviceMetricsCollector(DeviceDataAcquisition dataAcquisition, BiConsumer<String, DeviceMetricsCollector> consumer) {
        DeviceInfos deviceInfos = new DeviceInfos(dataAcquisition.getDeviceInfos());
        switch (dataAcquisition.getDataAcquisitionType()) {
            case BACNET:
                Map<String, List<BacnetDataPoint>> deviceDataPointsMap = dataAcquisition.getDataPoints()
                        .toLabels()
                        .stream()
                        .map(BacnetDataPoint::new)
                        .collect(Collectors.groupingBy(in -> in.getDataPointId().getDeviceCode()));
                for (Map.Entry<String, List<BacnetDataPoint>> entry : deviceDataPointsMap.entrySet()) {
                    String deviceCode = entry.getKey();
                    List<BacnetDataPoint> dataPoints = entry.getValue();
                    Labels deviceInfo = deviceInfos.get(deviceCode);
                    DeviceMetricsCollector deviceMetricsCollector = new BacnetDeviceMetricsCollector(deviceCode, deviceInfo, dataPoints);
                    consumer.accept(deviceCode, deviceMetricsCollector);
                }
        }
    }
}
