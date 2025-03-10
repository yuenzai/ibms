package cn.ecosync.ibms.gateway.bacnet;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.gateway.model.DeviceMetricsCollector;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.PropertyValues;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.model.snapshots.GaugeSnapshot;
import io.prometheus.metrics.model.snapshots.InfoSnapshot;
import io.prometheus.metrics.model.snapshots.Labels;
import io.prometheus.metrics.model.snapshots.MetricSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.Constants.LABEL_DEVICE_CODE;

public class BacnetDeviceMetricsCollector implements DeviceMetricsCollector {
    private static final Logger log = LoggerFactory.getLogger(BacnetDeviceMetricsCollector.class);

    private final String deviceCode;
    private final List<BacnetDataPoint> dataPoints;
    private final BacnetService bacnetService;

    private final Counter devicePointsScrapedTotal;
    private final Counter devicePointsScrapedSucceedTotal;
    private final InfoSnapshot deviceInfo;
    private final InfoSnapshot devicePointInfos;

    public BacnetDeviceMetricsCollector(String deviceCode, Labels deviceInfo, List<BacnetDataPoint> dataPoints, BacnetService bacnetService) {
        Assert.hasText(deviceCode, "deviceCode must not be null");
        Assert.notEmpty(dataPoints, "dataPoints must not be empty");
        this.deviceCode = deviceCode;
        this.dataPoints = Collections.unmodifiableList(new ArrayList<>(dataPoints));
        this.bacnetService = bacnetService;
        this.devicePointsScrapedTotal = Counter.builder()
                .name("ibms_device_points_scraped_total")
                .labelNames(LABEL_DEVICE_CODE)
                .build();
        this.devicePointsScrapedSucceedTotal = Counter.builder()
                .name("ibms_device_points_scraped_succeed_total")
                .labelNames(LABEL_DEVICE_CODE)
                .build();
        this.devicePointsScrapedTotal.initLabelValues(deviceCode);
        this.devicePointsScrapedSucceedTotal.initLabelValues(deviceCode);
        InfoSnapshot.Builder deviceInfoBuilder = InfoSnapshot.builder()
                .name("ibms_device");
        Optional.ofNullable(deviceInfo)
                .map(InfoSnapshot.InfoDataPointSnapshot::new)
                .ifPresent(deviceInfoBuilder::dataPoint);
        this.deviceInfo = deviceInfoBuilder.build();
        InfoSnapshot.Builder devicePointInfoBuilder = InfoSnapshot.builder()
                .name("ibms_device_point");
        dataPoints.stream()
                .map(BacnetDataPoint::getLabels)
                .map(InfoSnapshot.InfoDataPointSnapshot::new)
                .forEach(devicePointInfoBuilder::dataPoint);
        this.devicePointInfos = devicePointInfoBuilder.build();
    }

    @Override
    public void collect(Consumer<MetricSnapshot> metricsConsumer) {
        devicePointsScrapedTotal.labelValues(deviceCode).inc(dataPoints.size());
        Map<Integer, List<BacnetDataPoint>> map = dataPoints.stream()
                .collect(Collectors.groupingBy(BacnetDataPoint::getDeviceInstance));
        for (Map.Entry<Integer, List<BacnetDataPoint>> entry : map.entrySet()) {
            Integer deviceInstance = entry.getKey();
            try {
                collect(deviceInstance, entry.getValue(), in -> {
                    metricsConsumer.accept(in);
                    devicePointsScrapedSucceedTotal.labelValues(deviceCode).inc();
                });
            } catch (Exception e) {
                log.atError().setCause(e).addKeyValue("deviceInstance", deviceInstance).log("采集失败");
            }
        }
        metricsConsumer.accept(devicePointsScrapedTotal.collect());
        metricsConsumer.accept(devicePointsScrapedSucceedTotal.collect());
        metricsConsumer.accept(deviceInfo);
        metricsConsumer.accept(devicePointInfos);
    }

    private void collect(Integer deviceInstance, List<BacnetDataPoint> dataPoints, Consumer<MetricSnapshot> pointMetricConsumer) throws Exception {
        log.atInfo().addKeyValue("deviceInstance", deviceInstance).addKeyValue("deviceInstance", deviceInstance).log("采集开始");
        PropertyValues ack = scrape(deviceInstance, dataPoints);
        consume(dataPoints, ack, pointMetricConsumer);
        log.atInfo().addKeyValue("deviceInstance", deviceInstance).addKeyValue("deviceInstance", deviceInstance).log("采集结束");
    }

    private PropertyValues scrape(Integer deviceInstance, List<BacnetDataPoint> dataPoints) throws Exception {
        List<BacnetObjectProperties> bacnetDataPoints = dataPoints.stream()
                .map(BacnetObjectProperties::new)
                .collect(Collectors.toList());
        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, bacnetDataPoints);
        return doScrape(service);
    }

    private PropertyValues doScrape(BacnetReadPropertyMultipleService service) throws Exception {
        return bacnetService.execute(service);
    }

    private void consume(List<BacnetDataPoint> bacnetDataPoints, PropertyValues ack, Consumer<MetricSnapshot> pointMetricConsumer) {
        if (ack == null) return;
        for (BacnetDataPoint bacnetDataPoint : bacnetDataPoints) {
            BacnetObjectProperties objectProperties = bacnetDataPoint.toBacnetObjectProperties();
            BacnetObject bacnetObject = objectProperties.getBacnetObject();
            ObjectIdentifier oid = new ObjectIdentifier(bacnetObject.getObjectType().getCode(), bacnetObject.getObjectInstance());
            ObjectPropertyReference opr = new ObjectPropertyReference(oid, PropertyIdentifier.presentValue);
            Encodable encodable = PropertyValues.getNullOnError(ack.getNoErrorCheck(opr));
            Number presentValue = BacnetService.getValueAsNumber(encodable);
            if (presentValue == null) {
                log.atWarn().addKeyValue("bacnetObject", bacnetObject).log("ack缺少该对象的当前值");
                continue;
            }
            String metricName = bacnetDataPoint.getDataPointId().getMetricName();
            double value = presentValue.doubleValue();
            GaugeSnapshot.GaugeDataPointSnapshot dataPoint = new GaugeSnapshot.GaugeDataPointSnapshot(value, Labels.EMPTY, null);
            GaugeSnapshot metric = GaugeSnapshot.builder()
                    .name(metricName)
                    .dataPoint(dataPoint)
                    .build();
            pointMetricConsumer.accept(metric);
        }
    }
}
