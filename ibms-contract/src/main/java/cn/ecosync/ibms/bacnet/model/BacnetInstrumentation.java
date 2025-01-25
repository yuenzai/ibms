package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.JsonSerdeContextHolder;
import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.device.model.DeviceDataPointLabel;
import cn.ecosync.ibms.metrics.Instrument;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.serde.TypeReference;
import cn.ecosync.iframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.bacnet.dto.BacnetProperty.PROPERTY_PRESENT_VALUE;

public class BacnetInstrumentation implements Instrument {
    private static final Logger log = LoggerFactory.getLogger(BacnetInstrumentation.class);
    private static final String SEGMENTATION_NOT_SUPPORTED = "BACnet Abort: Segmentation Not Supported\n";

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
        return scrape(service);
    }

    private ReadPropertyMultipleAck scrape(BacnetReadPropertyMultipleService service) {
        try {
            return doScrape(service);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ReadPropertyMultipleAck doScrape(BacnetReadPropertyMultipleService service) throws IOException, InterruptedException {
        List<String> command = service.toCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        log.info("Execute command[{}]", String.join(" ", command));
        if (StringUtils.hasText(stdout)) log.info("{}", stdout);
        if (SEGMENTATION_NOT_SUPPORTED.equals(stderr)) throw new SegmentationNotSupportedException();
        if (StringUtils.hasText(stderr)) throw new RuntimeException(stderr);
        JsonSerde jsonSerde = JsonSerdeContextHolder.get();
        return jsonSerde.deserialize(stdout, new TypeReference<ReadPropertyMultipleAck>() {
        });
    }

    private static class SegmentationNotSupportedException extends RuntimeException {
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
