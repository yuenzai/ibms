//package cn.ecosync.ibms.bacnet.model;
//
//import cn.ecosync.ibms.bacnet.dto.*;
//import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService.SegmentationNotSupportedException;
//import cn.ecosync.ibms.gateway.model.DeviceMetricsCollector;
//import io.prometheus.metrics.core.metrics.Counter;
//import io.prometheus.metrics.model.snapshots.GaugeSnapshot;
//import io.prometheus.metrics.model.snapshots.InfoSnapshot;
//import io.prometheus.metrics.model.snapshots.Labels;
//import io.prometheus.metrics.model.snapshots.MetricSnapshot;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.support.PagedListHolder;
//import org.springframework.util.Assert;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.function.BiConsumer;
//import java.util.function.Consumer;
//import java.util.stream.Collectors;
//
//import static cn.ecosync.ibms.Constants.LABEL_DEVICE_CODE;
//import static cn.ecosync.ibms.bacnet.dto.BacnetProperty.PROPERTY_PRESENT_VALUE;
//
//public class BacnetDeviceMetricsCollector implements DeviceMetricsCollector {
//    private static final Logger log = LoggerFactory.getLogger(BacnetDeviceMetricsCollector.class);
//    private static final int MAX_SEGMENTATION = 6;
//
//    private final String deviceCode;
//    private final List<BacnetDataPoint> dataPoints;
//    private final Map<Integer, AtomicInteger> deviceInstanceSegmentations = new ConcurrentHashMap<>();
//
//    private final Counter devicePointsScrapedTotal;
//    private final Counter devicePointsScrapedSucceedTotal;
//    private final InfoSnapshot deviceInfo;
//    private final InfoSnapshot devicePointInfos;
//
//    public BacnetDeviceMetricsCollector(String deviceCode, Labels deviceInfo, List<BacnetDataPoint> dataPoints) {
//        Assert.hasText(deviceCode, "deviceCode must not be null");
//        Assert.notEmpty(dataPoints, "dataPoints must not be empty");
//        this.deviceCode = deviceCode;
//        this.dataPoints = Collections.unmodifiableList(new ArrayList<>(dataPoints));
//        this.devicePointsScrapedTotal = Counter.builder()
//                .name("ibms_device_points_scraped_total")
//                .labelNames(LABEL_DEVICE_CODE)
//                .build();
//        this.devicePointsScrapedSucceedTotal = Counter.builder()
//                .name("ibms_device_points_scraped_succeed_total")
//                .labelNames(LABEL_DEVICE_CODE)
//                .build();
//        this.devicePointsScrapedTotal.initLabelValues(deviceCode);
//        this.devicePointsScrapedSucceedTotal.initLabelValues(deviceCode);
//        InfoSnapshot.Builder deviceInfoBuilder = InfoSnapshot.builder()
//                .name("ibms_device");
//        Optional.ofNullable(deviceInfo)
//                .map(InfoSnapshot.InfoDataPointSnapshot::new)
//                .ifPresent(deviceInfoBuilder::dataPoint);
//        this.deviceInfo = deviceInfoBuilder.build();
//        InfoSnapshot.Builder devicePointInfoBuilder = InfoSnapshot.builder()
//                .name("ibms_device_point");
//        dataPoints.stream()
//                .map(BacnetDataPoint::getLabels)
//                .map(InfoSnapshot.InfoDataPointSnapshot::new)
//                .forEach(devicePointInfoBuilder::dataPoint);
//        this.devicePointInfos = devicePointInfoBuilder.build();
//    }
//
//    @Override
//    public void collect(Consumer<MetricSnapshot> metricsConsumer) {
//        devicePointsScrapedTotal.labelValues(deviceCode).inc(dataPoints.size());
//        Map<Integer, List<BacnetDataPoint>> map = dataPoints.stream()
//                .collect(Collectors.groupingBy(BacnetDataPoint::getDeviceInstance));
//        for (Map.Entry<Integer, List<BacnetDataPoint>> entry : map.entrySet()) {
//            try {
//                collect(entry.getKey(), entry.getValue(), in -> {
//                    metricsConsumer.accept(in);
//                    devicePointsScrapedSucceedTotal.labelValues(deviceCode).inc();
//                });
//            } catch (Exception e) {
//                log.atError().setCause(e).log("");
//            }
//        }
//        metricsConsumer.accept(devicePointsScrapedTotal.collect());
//        metricsConsumer.accept(devicePointsScrapedSucceedTotal.collect());
//        metricsConsumer.accept(deviceInfo);
//        metricsConsumer.accept(devicePointInfos);
//    }
//
//    private void collect(Integer deviceInstance, List<BacnetDataPoint> dataPoints, Consumer<MetricSnapshot> pointMetricConsumer) {
//        AtomicInteger segmentationIncrementer = deviceInstanceSegmentations.computeIfAbsent(deviceInstance, in -> new AtomicInteger(1));
//        int segmentation = segmentationIncrementer.get();
//        log.atInfo().addKeyValue("deviceInstance", deviceInstance).addKeyValue("segmentation", segmentation).log("采集开始");
//        try {
//            paging(dataPoints, segmentation, (pageNumber, page) -> {
//                ReadPropertyMultipleAck ack = scrape(deviceInstance, page);
//                consume(page, ack, pointMetricConsumer);
//            });
//        } catch (SegmentationNotSupportedException e) {
//            log.atWarn().log("设备不支持分段传输");
//            if (segmentation < MAX_SEGMENTATION) {
//                segmentationIncrementer.incrementAndGet();
//            }
//        }
//        log.atInfo().addKeyValue("deviceInstance", deviceInstance).log("采集结束");
//    }
//
//    private ReadPropertyMultipleAck scrape(Integer deviceInstance, List<BacnetDataPoint> dataPoints) {
//        List<BacnetObjectProperties> bacnetDataPoints = dataPoints.stream()
//                .map(BacnetObjectProperties::new)
//                .collect(Collectors.toList());
//        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, bacnetDataPoints);
//        return doScrape(service);
//    }
//
//    private ReadPropertyMultipleAck doScrape(BacnetReadPropertyMultipleService service) {
//        try {
//            return BacnetReadPropertyMultipleService.execute(service);
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void consume(List<BacnetDataPoint> bacnetDataPoints, ReadPropertyMultipleAck ack, Consumer<MetricSnapshot> pointMetricConsumer) {
//        if (ack == null) return;
//        Map<BacnetObject, Map<BacnetProperty, BacnetPropertyResult>> propertiesMap = ack.toMap();
//        for (BacnetDataPoint bacnetDataPoint : bacnetDataPoints) {
//            BacnetObjectProperties objectProperties = bacnetDataPoint.toBacnetObjectProperties();
//            BacnetObject bacnetObject = objectProperties.getBacnetObject();
//            BacnetPropertyValue presentValue = Optional.ofNullable(propertiesMap.get(bacnetObject))
//                    .map(in -> in.get(PROPERTY_PRESENT_VALUE))
//                    .map(BacnetPropertyResult::getValue)
//                    .orElse(null);
//            if (presentValue == null) {
//                log.atWarn().addKeyValue("bacnetObject", bacnetObject).log("ack缺少该对象的当前值");
//                continue;
//            }
//            String metricName = bacnetDataPoint.getDataPointId().getMetricName();
//            double value = presentValue.getValueAsNumber().doubleValue();
//            GaugeSnapshot.GaugeDataPointSnapshot dataPoint = new GaugeSnapshot.GaugeDataPointSnapshot(value, Labels.EMPTY, null);
//            GaugeSnapshot metric = GaugeSnapshot.builder()
//                    .name(metricName)
//                    .dataPoint(dataPoint)
//                    .build();
//            pointMetricConsumer.accept(metric);
//        }
//    }
//
//    public <T> void paging(List<T> list, int pageCount, BiConsumer<Integer, List<T>> pageConsumer) {
//        int pageSize = list.size() / pageCount;
//        Assert.isTrue(pageSize > 0, "pageSize must be greater than 0");
//        PagedListHolder<T> pagedListHolder = new PagedListHolder<>(list);
//        pagedListHolder.setPageSize(pageSize);
//        for (int i = 0; i <= pageCount; i++) {
//            List<T> page = pagedListHolder.getPageList();
//            pageConsumer.accept(pagedListHolder.getPage(), page);
//            if (pagedListHolder.isLastPage()) break;
//            pagedListHolder.nextPage();
//        }
//    }
//}
