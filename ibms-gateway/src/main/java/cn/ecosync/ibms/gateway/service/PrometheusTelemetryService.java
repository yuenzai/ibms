package cn.ecosync.ibms.gateway.service;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.StringUtils;
import io.prometheus.metrics.model.registry.MultiCollector;
import io.prometheus.metrics.model.registry.PrometheusScrapeRequest;
import io.prometheus.metrics.model.snapshots.MetricSnapshots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrometheusTelemetryService implements TelemetryService, MultiCollector {
    private static final Logger log = LoggerFactory.getLogger(PrometheusTelemetryService.class);

    private final Map<DeviceDataAcquisitionId, DeviceDataAcquisition> dataAcquisitionMap = new ConcurrentHashMap<>();
    private final Map<String, MultiCollector> instruments = new ConcurrentHashMap<>();

    @Override
    public void add(DeviceDataAcquisition... deviceDataAcquisitions) {
        for (DeviceDataAcquisition deviceDataAcquisition : deviceDataAcquisitions) {
            dataAcquisitionMap.put(deviceDataAcquisition.getDataAcquisitionId(), deviceDataAcquisition);
        }
    }

    @Override
    public void remove(DeviceDataAcquisitionId deviceDataAcquisitionId) {
        dataAcquisitionMap.remove(deviceDataAcquisitionId);
    }

    @Override
    public void set(List<DeviceDataAcquisition> dataAcquisitions) {
        Map<DeviceDataAcquisitionId, DeviceDataAcquisition> dataAcquisitionMap = dataAcquisitions.stream()
                .collect(Collectors.toMap(DeviceDataAcquisition::getDataAcquisitionId, Function.identity()));
        this.dataAcquisitionMap.clear();
        this.dataAcquisitionMap.putAll(dataAcquisitionMap);
    }

    @Override
    public void reload() {
        Map<String, MultiCollector> instruments = new HashMap<>();
        for (DeviceDataAcquisition dataAcquisition : dataAcquisitionMap.values()) {
            dataAcquisition.getDataPoints().newInstruments(instruments::put);
        }
        this.instruments.clear();
        this.instruments.putAll(instruments);
    }

    @Override
    public MetricSnapshots collect(PrometheusScrapeRequest scrapeRequest) {
        String deviceCode = CollectionUtils.firstElement(Arrays.asList(scrapeRequest.getParameterValues("target")));
        log.info("collect(requestPath={}, target={})", scrapeRequest.getRequestPath(), deviceCode);
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

//    如果实现了 getPrometheusNames 方法，每次更新配置后需要重新注册（PrometheusRegistry::register）
//    @Override
//    public List<String> getPrometheusNames() {
//        return MultiCollector.super.getPrometheusNames();
//    }
}
