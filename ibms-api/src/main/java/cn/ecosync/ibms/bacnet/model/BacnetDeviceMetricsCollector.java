package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService.SegmentationNotSupportedException;
import cn.ecosync.ibms.gateway.model.DeviceMetricsCollector;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.GaugeSnapshot;
import io.prometheus.metrics.model.snapshots.InfoSnapshot;
import io.prometheus.metrics.model.snapshots.Labels;
import io.prometheus.metrics.model.snapshots.MetricSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.bacnet.dto.BacnetProperty.PROPERTY_PRESENT_VALUE;

public class BacnetDeviceMetricsCollector implements DeviceMetricsCollector {
    private static final Logger log = LoggerFactory.getLogger(BacnetDeviceMetricsCollector.class);

    private final String deviceCode;
    private final List<BacnetDataPoint> dataPoints;
    private final AtomicInteger segmentationCount = new AtomicInteger(1);
    private volatile boolean infiniteLoopOccurred = false;

    private final Gauge deviceScrapeStatus;
    private final InfoSnapshot deviceInfo;
    private final InfoSnapshot devicePointInfos;

    public BacnetDeviceMetricsCollector(String deviceCode, Labels deviceInfo, List<BacnetDataPoint> dataPoints) {
        Assert.hasText(deviceCode, "deviceCode must not be null");
        Assert.notEmpty(dataPoints, "dataPoints must not be empty");
        this.deviceCode = deviceCode;
        this.dataPoints = dataPoints;
        this.deviceScrapeStatus = Gauge.builder()
                .name("ibms_device_scrape_status")
                .labelNames("device_code")
                .build();
        InfoSnapshot.Builder deviceInfoBuilder = InfoSnapshot.builder()
                .name("ibms_device")
                .help("IBMS Device Info");
        Optional.ofNullable(deviceInfo)
                .map(InfoSnapshot.InfoDataPointSnapshot::new)
                .ifPresent(deviceInfoBuilder::dataPoint);
        this.deviceInfo = deviceInfoBuilder.build();
        InfoSnapshot.Builder devicePointInfoBuilder = InfoSnapshot.builder()
                .name("ibms_device_point")
                .help("IBMS Device Point Info");
        dataPoints.stream()
                .map(BacnetDataPoint::getLabels)
                .map(InfoSnapshot.InfoDataPointSnapshot::new)
                .forEach(devicePointInfoBuilder::dataPoint);
        this.devicePointInfos = devicePointInfoBuilder.build();
    }

    @Override
    public void collect(Consumer<MetricSnapshot> metricsConsumer) {
        Assert.state(!infiniteLoopOccurred, "infinite loop occurred");
        Assert.state(segmentationCount.get() <= 5, "maxSegmentationCount must be less than 5");
        Map<Integer, List<BacnetDataPoint>> map = dataPoints.stream()
                .collect(Collectors.groupingBy(BacnetDataPoint::getDeviceInstance));
        for (Map.Entry<Integer, List<BacnetDataPoint>> entry : map.entrySet()) {
            for (int i = 0; i < 10; i++) {
                try {
                    collect(entry.getKey(), entry.getValue(), metricsConsumer::accept);
                    deviceScrapeStatus.labelValues(deviceCode).set(1);
                    break;
                } catch (SegmentationNotSupportedException e) {
                    log.atWarn().log("设备不支持分段传输");
                    segmentationCount.incrementAndGet();
                } catch (Exception e) {
                    deviceScrapeStatus.labelValues(deviceCode).set(0);
                    log.atError().setCause(e).log("");
                    break;
                }
                if (i == 9) infiniteLoopOccurred = true;
            }
        }
        metricsConsumer.accept(deviceScrapeStatus.collect());
        metricsConsumer.accept(deviceInfo);
        metricsConsumer.accept(devicePointInfos);
    }

    private void collect(Integer deviceInstance, List<BacnetDataPoint> dataPoints, Consumer<GaugeSnapshot> pointMetricConsumer) {
        Assert.isTrue(segmentationCount.get() <= 5, "maxSegmentationCount must be less than 5");
        if (segmentationCount.get() == 1) {
            ReadPropertyMultipleAck ack = scrape(deviceInstance, dataPoints);
            consume(dataPoints, ack, pointMetricConsumer);
        } else {
            log.atInfo()
                    .addKeyValue("deviceInstance", deviceInstance)
                    .addKeyValue("segmentationCount", segmentationCount.get())
                    .log("分段采集开始");
            int pageSize = dataPoints.size() / segmentationCount.get();
            paging(dataPoints, pageSize, (pageNumber, page) -> {
                ReadPropertyMultipleAck ack = scrape(deviceInstance, page);
                consume(page, ack, pointMetricConsumer);
            });
            log.atInfo().addKeyValue("deviceInstance", deviceInstance).log("分段采集结束");
        }
    }

    private ReadPropertyMultipleAck scrape(Integer deviceInstance, List<BacnetDataPoint> dataPoints) {
        List<BacnetObjectProperties> bacnetDataPoints = dataPoints.stream()
                .map(BacnetObjectProperties::new)
                .collect(Collectors.toList());
        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, bacnetDataPoints);
        return doScrape(service);
    }

    private ReadPropertyMultipleAck doScrape(BacnetReadPropertyMultipleService service) {
        try {
            return BacnetReadPropertyMultipleService.execute(service);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void consume(List<BacnetDataPoint> bacnetDataPoints, ReadPropertyMultipleAck ack, Consumer<GaugeSnapshot> pointMetricConsumer) {
        if (ack == null) return;
        Map<BacnetObject, Map<BacnetProperty, BacnetPropertyResult>> propertiesMap = ack.toMap();
        for (BacnetDataPoint bacnetDataPoint : bacnetDataPoints) {
            BacnetObjectProperties objectProperties = bacnetDataPoint.toBacnetObjectProperties();
            BacnetObject bacnetObject = objectProperties.getBacnetObject();
            BacnetPropertyValue presentValue = Optional.ofNullable(propertiesMap.get(bacnetObject))
                    .map(in -> in.get(PROPERTY_PRESENT_VALUE))
                    .map(BacnetPropertyResult::getValue)
                    .orElse(null);
            if (presentValue == null) {
                log.atWarn().addKeyValue("bacnetObject", bacnetObject).log("ack缺少该对象的当前值");
                continue;
            }
            String metricName = bacnetDataPoint.getDataPointId().getMetricName();
            double value = presentValue.getValueAsNumber().doubleValue();
            GaugeSnapshot.GaugeDataPointSnapshot dataPoint = new GaugeSnapshot.GaugeDataPointSnapshot(value, Labels.EMPTY, null);
            GaugeSnapshot metric = GaugeSnapshot.builder()
                    .name(metricName)
                    .dataPoint(dataPoint)
                    .build();
            pointMetricConsumer.accept(metric);
        }
    }

    public <T> void paging(List<T> list, int pageSize, BiConsumer<Integer, List<T>> pageConsumer) {
        Assert.isTrue(pageSize > 0, "pageSize must be greater than 0");
        PagedListHolder<T> pagedListHolder = new PagedListHolder<>(list);
        pagedListHolder.setPageSize(pageSize);
        for (int i = 0; i < 10; i++) {
            List<T> page = pagedListHolder.getPageList();
            pageConsumer.accept(pagedListHolder.getPage(), page);
            if (pagedListHolder.isLastPage()) {
                break;
            }
            pagedListHolder.nextPage();
        }
    }
}
