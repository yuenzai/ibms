package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService.SegmentationNotSupportedException;
import cn.ecosync.ibms.gateway.model.DeviceDataPoint;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.core.metrics.Info;
import io.prometheus.metrics.model.registry.MultiCollector;
import io.prometheus.metrics.model.snapshots.GaugeSnapshot;
import io.prometheus.metrics.model.snapshots.Labels;
import io.prometheus.metrics.model.snapshots.MetricSnapshots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.bacnet.dto.BacnetProperty.PROPERTY_PRESENT_VALUE;

public class BacnetInstrumentation implements MultiCollector {
    private static final Logger log = LoggerFactory.getLogger(BacnetInstrumentation.class);

    private final List<BacnetDataPoint> dataPoints;
    private final AtomicInteger segmentationCount = new AtomicInteger(1);

    private final Counter scrapeCount;
    private final Counter scrapeFailureCount;
    private final Info pointInfo;

    public BacnetInstrumentation(List<BacnetDataPoint> dataPoints) {
        Assert.notEmpty(dataPoints, "dataPoints must not be empty");
        this.dataPoints = dataPoints;
        this.scrapeCount = Counter.builder()
                .name("scrape_total")
                .help("Total number of scrape")
                .build();
        this.scrapeFailureCount = Counter.builder()
                .name("scrape_failure_total")
                .help("Total number of scrape failure")
                .build();
        Info pointInfo = dataPoints.get(0)
                .toPointInfo();
        dataPoints.stream()
                .map(DeviceDataPoint::toLabelValues)
                .forEach(pointInfo::addLabelValues);
        this.pointInfo = pointInfo;
    }

    @Override
    public MetricSnapshots collect() {
        Assert.isTrue(segmentationCount.get() <= 5, "maxSegmentationCount must be less than 5");
        Map<Integer, List<BacnetDataPoint>> map = dataPoints.stream()
                .collect(Collectors.groupingBy(BacnetDataPoint::getDeviceInstance));
        MetricSnapshots.Builder metricsBuilder = MetricSnapshots.builder();
        for (Map.Entry<Integer, List<BacnetDataPoint>> entry : map.entrySet()) {
            while (true) {
                try {
                    collect(entry.getKey(), entry.getValue(), metricsBuilder::metricSnapshot);
                    scrapeCount.inc();
                    break;
                } catch (SegmentationNotSupportedException e) {
                    log.error("设备不支持分段传输");
                    segmentationCount.incrementAndGet();
                } catch (Exception e) {
                    scrapeFailureCount.inc();
                    scrapeCount.inc();
                    throw e;
                }
            }
        }
        metricsBuilder.metricSnapshot(scrapeCount.collect());
        metricsBuilder.metricSnapshot(scrapeFailureCount.collect());
        metricsBuilder.metricSnapshot(pointInfo.collect());
        return metricsBuilder.build();
    }

    private void collect(Integer deviceInstance, List<BacnetDataPoint> dataPoints, Consumer<GaugeSnapshot> pointMetricConsumer) {
        Assert.isTrue(segmentationCount.get() <= 5, "maxSegmentationCount must be less than 5");
        if (segmentationCount.get() == 1) {
            ReadPropertyMultipleAck ack = scrape(deviceInstance, dataPoints);
            consume(deviceInstance, dataPoints, ack, pointMetricConsumer);
        } else {
            log.info("开始分段采集[deviceInstance={}, segmentationCount={}]", deviceInstance, segmentationCount.get());
            int pageSize = dataPoints.size() / segmentationCount.get();
            paging(dataPoints, pageSize, (pageNumber, page) -> {
                ReadPropertyMultipleAck ack = scrape(deviceInstance, page);
                consume(deviceInstance, page, ack, pointMetricConsumer);
            });
            log.info("分段采集结束[deviceInstance={}]", deviceInstance);
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
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void consume(Integer deviceInstance, List<BacnetDataPoint> bacnetDataPoints, ReadPropertyMultipleAck ack, Consumer<GaugeSnapshot> pointMetricConsumer) {
        Map<BacnetObject, Map<BacnetProperty, BacnetPropertyResult>> propertiesMap = ack.toMap();
        for (BacnetDataPoint bacnetDataPoint : bacnetDataPoints) {
            BacnetObjectProperties objectProperties = bacnetDataPoint.toBacnetObjectProperties();
            Map<BacnetProperty, BacnetPropertyResult> propertyMap = propertiesMap.get(objectProperties.getBacnetObject());
            BacnetPropertyValue presentValue = Optional.ofNullable(propertyMap.get(PROPERTY_PRESENT_VALUE))
                    .map(BacnetPropertyResult::getValue)
                    .orElse(null);
            if (presentValue == null) continue;
            String metricName = bacnetDataPoint.getDataPointId().getMetricName();
            double value = presentValue.getValueAsNumber().doubleValue();
            // {metric_type = "device"} * on (instance, metric_name) group_left (...) point_info
            String[] labelNames = {"device_instance", "metric_type", "metric_name"};
            String[] labelValues = {deviceInstance.toString(), "device", metricName};
            Labels labels = Labels.of(labelNames, labelValues);
            GaugeSnapshot.GaugeDataPointSnapshot dataPoint = new GaugeSnapshot.GaugeDataPointSnapshot(value, labels, null);
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
        while (true) {
            List<T> page = pagedListHolder.getPageList();
            pageConsumer.accept(pagedListHolder.getPage(), page);
            if (pagedListHolder.isLastPage()) {
                break;
            }
            pagedListHolder.nextPage();
        }
    }
}
