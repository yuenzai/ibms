package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.iframework.util.CollectionUtils;
import io.prometheus.metrics.model.registry.MultiCollector;
import lombok.ToString;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@ToString(callSuper = true)
public class BacnetDataAcquisition extends DeviceDataAcquisition {
    private List<BacnetDataPoint> dataPoints;

    protected BacnetDataAcquisition() {
    }

    public BacnetDataAcquisition(DeviceDataAcquisition dataAcquisition, List<BacnetDataPoint> dataPoints) {
        this(dataAcquisition.getDataAcquisitionId(), dataAcquisition.getScrapeInterval(), dataPoints);
    }

    public BacnetDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval, List<BacnetDataPoint> dataPoints) {
        super(dataAcquisitionId, scrapeInterval);
        this.dataPoints = dataPoints;
    }

    @Override
    public List<BacnetDataPoint> getDataPoints() {
        return CollectionUtils.nullSafeOf(dataPoints);
    }

    @Override
    public BacnetDataAcquisition withScrapeInterval(Long scrapeInterval) {
        return new BacnetDataAcquisition(getDataAcquisitionId(), scrapeInterval, getDataPoints());
    }

    @Override
    public void newInstruments(BiConsumer<String, MultiCollector> consumer) {
        getDataPoints().stream()
                .collect(Collectors.groupingBy(in -> in.getDataPointId().getDeviceCode()))
                .forEach((deviceCode, dataPoints) -> consumer.accept(deviceCode, new BacnetInstrumentation(dataPoints)));
    }
}
