package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService.SegmentationNotSupportedException;
import cn.ecosync.ibms.device.model.DeviceDataPointLabel;
import cn.ecosync.ibms.metrics.Instrument;
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
import java.util.stream.Collectors;

import static cn.ecosync.ibms.bacnet.dto.BacnetProperty.PROPERTY_PRESENT_VALUE;

public class BacnetInstrumentation implements Instrument {
    private static final Logger log = LoggerFactory.getLogger(BacnetInstrumentation.class);

    private final List<BacnetDataPoint> dataPoints;
    private final AtomicInteger segmentationCount = new AtomicInteger(1);

    public BacnetInstrumentation(List<BacnetDataPoint> dataPoints) {
        Assert.notEmpty(dataPoints, "dataPoints must not be empty");
        this.dataPoints = dataPoints;
    }

    @Override
    public List<BacnetDataPoint> getDataPoints() {
        return dataPoints;
    }

    @Override
    public void collectMetrics(MetricsCollector metricsCollector) {
        Assert.notNull(metricsCollector, "metricsCollector must not be null");
        Assert.isTrue(segmentationCount.get() <= 5, "maxSegmentationCount must be less than 5");
        Map<Integer, List<BacnetDataPoint>> map = dataPoints.stream()
                .collect(Collectors.groupingBy(BacnetDataPoint::getDeviceInstance));
        for (Map.Entry<Integer, List<BacnetDataPoint>> entry : map.entrySet()) {
            while (true) {
                try {
                    collect(entry.getKey(), entry.getValue(), new ScrapeCallback(metricsCollector));
                    break;
                } catch (SegmentationNotSupportedException e) {
                    log.error("设备不支持分段传输");
                    segmentationCount.incrementAndGet();
                }
            }
        }
    }

    private void collect(Integer deviceInstance, List<BacnetDataPoint> dataPoints, ScrapeCallback ackCallback) {
        Assert.isTrue(segmentationCount.get() <= 5, "maxSegmentationCount must be less than 5");
        if (segmentationCount.get() == 1) {
            ReadPropertyMultipleAck ack = scrape(deviceInstance, dataPoints);
            ackCallback.callback(deviceInstance, dataPoints, ack);
        } else {
            log.info("开始分段采集[deviceInstance={}, segmentationCount={}]", deviceInstance, segmentationCount.get());
            int pageSize = dataPoints.size() / segmentationCount.get();
            paging(dataPoints, pageSize, (pageNumber, page) -> {
                ReadPropertyMultipleAck ack = scrape(deviceInstance, page);
                ackCallback.callback(deviceInstance, page, ack);
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
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ScrapeCallback {
        private final MetricsCollector metricsCollector;

        public ScrapeCallback(MetricsCollector metricsCollector) {
            this.metricsCollector = metricsCollector;
        }

        private void callback(Integer deviceInstance, List<BacnetDataPoint> dataPoints, ReadPropertyMultipleAck ack) {
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
    }

    public static <T> void paging(List<T> list, int pageSize, BiConsumer<Integer, List<T>> pageConsumer) {
        Assert.isTrue(pageSize > 0, "pageSize must be greater than 0");
        PagedListHolder<T> pagedListHolder = new PagedListHolder<>(list);
        pagedListHolder.setPageSize(pageSize);
        while (true) {
            List<T> page = pagedListHolder.getPageList();
            if (pageConsumer != null) pageConsumer.accept(pagedListHolder.getPage(), page);
            if (pagedListHolder.isLastPage()) {
                break;
            }
            pagedListHolder.nextPage();
        }
    }
}
