package cn.ecosync.ibms.gateway.service;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.LabelTable;
import cn.ecosync.ibms.gateway.model.LabelValues;
import io.prometheus.metrics.model.registry.MultiCollector;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.prometheus.metrics.model.snapshots.*;
import io.prometheus.metrics.model.snapshots.InfoSnapshot.InfoDataPointSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GatewayMetricsTelemetryService implements MultiCollector {
    private static final Logger log = LoggerFactory.getLogger(GatewayMetricsTelemetryService.class);

    private final PrometheusRegistry prometheusRegistry;
    private final AtomicReference<List<DeviceDataAcquisition>> dataAcquisitionsRef = new AtomicReference<>(new ArrayList<>());

    public GatewayMetricsTelemetryService(PrometheusRegistry prometheusRegistry) {
        this.prometheusRegistry = prometheusRegistry;
    }

    public void reload(DeviceDataAcquisition... dataAcquisitions) {
        log.atInfo().log("重新加载配置");
        prometheusRegistry.clear();
        prometheusRegistry.register(this);
        dataAcquisitionsRef.set(Arrays.asList(dataAcquisitions));
    }

    @Override
    public MetricSnapshots collect() {
        List<DeviceDataAcquisition> dataAcquisitions = dataAcquisitionsRef.get();
        MetricSnapshots.Builder metricsBuilder = MetricSnapshots.builder();
        for (DeviceDataAcquisition dataAcquisition : dataAcquisitions) {
            InfoSnapshot.Builder deviceInfoBuilder = InfoSnapshot.builder()
                    .name("ibms_device")
                    .help("IBMS Device Info");
            LabelTable deviceInfos = dataAcquisition.getDeviceInfos();
            collect(deviceInfos, deviceInfoBuilder, metricsBuilder::metricSnapshot);

            InfoSnapshot.Builder pointInfoBuilder = InfoSnapshot.builder()
                    .name("ibms_device_point")
                    .help("IBMS Point Info");
            LabelTable dataPoints = dataAcquisition.getDataPoints();
            collect(dataPoints, pointInfoBuilder, metricsBuilder::metricSnapshot);
        }
        return metricsBuilder.build();
    }

    private void collect(LabelTable deviceInfos, InfoSnapshot.Builder infoBuilder, Consumer<MetricSnapshot> metricConsumer) {
        List<String> labelNames = deviceInfos.toLabelNames().stream()
                .map(PrometheusNaming::sanitizeMetricName)
                .collect(Collectors.toList());
        for (LabelValues deviceInfo : deviceInfos.toLabelValues()) {
            List<String> labelValues = deviceInfo.getLabelValues();
            Labels labels = Labels.of(labelNames, labelValues);
            InfoDataPointSnapshot infoDataPoint = new InfoDataPointSnapshot(labels);
            infoBuilder.dataPoint(infoDataPoint);
        }
        InfoSnapshot infoSnapshot = infoBuilder.build();
        metricConsumer.accept(infoSnapshot);
    }
}
