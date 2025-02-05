package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.DeviceDataPoints;
import cn.ecosync.ibms.util.CollectionUtils;
import io.prometheus.metrics.model.registry.MultiCollector;
import jakarta.validation.Valid;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@ToString
public class BacnetDataPoints implements DeviceDataPoints {
    private List<@Valid BacnetDataPoint> dataPoints;

    protected BacnetDataPoints() {
    }

    public BacnetDataPoints(List<BacnetDataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    @Override
    public void newInstruments(BiConsumer<String, MultiCollector> consumer) {
        toCollection().stream()
                .collect(Collectors.groupingBy(in -> in.getDataPointId().getDeviceCode()))
                .forEach((deviceCode, dataPoints) -> consumer.accept(deviceCode, new BacnetInstrumentation(dataPoints)));
    }

    @Override
    public Collection<BacnetDataPoint> toCollection() {
        return CollectionUtils.nullSafeOf(dataPoints);
    }

    public List<BacnetDataPoint> getDataPoints() {
        return dataPoints;
    }
}
