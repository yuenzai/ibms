package cn.ecosync.ibms.gateway.service;

import cn.ecosync.ibms.bacnet.dto.BacnetWhoIsService;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.StringUtils;
import io.prometheus.metrics.model.registry.MultiCollector;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.prometheus.metrics.model.registry.PrometheusScrapeRequest;
import io.prometheus.metrics.model.snapshots.MetricSnapshots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DeviceTelemetryService implements MultiCollector {
    private static final Logger log = LoggerFactory.getLogger(DeviceTelemetryService.class);

    private final PrometheusRegistry prometheusRegistry;
    private final AtomicReference<Map<String, MultiCollector>> instrumentsRef = new AtomicReference<>(new HashMap<>());

    public DeviceTelemetryService(PrometheusRegistry prometheusRegistry) {
        this.prometheusRegistry = prometheusRegistry;
    }

    public void reload(DeviceDataAcquisition... dataAcquisitions) {
        log.info("reload...");
        prometheusRegistry.clear();
        prometheusRegistry.register(this);
        Map<String, MultiCollector> instruments = new HashMap<>();
        for (DeviceDataAcquisition dataAcquisition : dataAcquisitions) {
            dataAcquisition.getDataPoints().newInstruments(instruments::put);
        }
        instrumentsRef.set(instruments);

        try {
            BacnetWhoIsService service = new BacnetWhoIsService();
            BacnetWhoIsService.execute(service);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public MetricSnapshots collect(PrometheusScrapeRequest scrapeRequest) {
        String deviceCode = CollectionUtils.firstElement(Arrays.asList(scrapeRequest.getParameterValues("target")));
        log.info("collect(requestPath={}, target={})", scrapeRequest.getRequestPath(), deviceCode);
        Map<String, MultiCollector> instruments = instrumentsRef.get();
        return Optional.ofNullable(deviceCode)
                .filter(StringUtils::hasText)
                .map(instruments::get)
                .map(MultiCollector::collect)
                .orElseGet(this::collect);
    }

    @Override
    public MetricSnapshots collect() {
        return new MetricSnapshots();
    }
}
