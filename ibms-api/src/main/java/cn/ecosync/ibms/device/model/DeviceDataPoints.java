package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataPoints;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.prometheus.metrics.model.registry.MultiCollector;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiConsumer;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceDataPoints.Empty.class, name = "EMPTY"),
        @JsonSubTypes.Type(value = BacnetDataPoints.class, name = "BACNET"),
})
public interface DeviceDataPoints {
    void newInstruments(BiConsumer<String, MultiCollector> consumer);

    default Collection<? extends DeviceDataPoint> toCollection() {
        return Collections.emptyList();
    }

    class Empty implements DeviceDataPoints {
        public static final Empty INSTANCE = new Empty();

        @Override
        public void newInstruments(BiConsumer<String, MultiCollector> consumer) {
        }
    }
}
