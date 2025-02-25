package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectType;
import cn.ecosync.ibms.gateway.model.DeviceDataPointId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.prometheus.metrics.model.snapshots.Labels;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.util.Optional;

import static cn.ecosync.ibms.Constants.LABEL_DEVICE_CODE;
import static cn.ecosync.ibms.Constants.LABEL_METRIC_NAME;

@ToString(callSuper = true)
public class BacnetDataPoint {
    private Labels labels;
    @Valid
    @JsonUnwrapped
    private DeviceDataPointId dataPointId;
    @NotNull
    private Integer deviceInstance;
    @Valid
    @JsonUnwrapped
    private BacnetObject bacnetObject;

    protected BacnetDataPoint() {
    }

    public BacnetDataPoint(Labels labels) {
        this.labels = labels;
        String deviceCode = labels.get(LABEL_DEVICE_CODE);
        String metricName = labels.get(LABEL_METRIC_NAME);
        this.dataPointId = new DeviceDataPointId(metricName, deviceCode);
        this.deviceInstance = Optional.ofNullable(labels.get("device_instance")).map(Integer::parseInt).orElse(null);
        BacnetObjectType objectType = Optional.ofNullable(labels.get("object_type")).map(BacnetObjectType::valueOf).orElse(null);
        Integer objectInstance = Optional.ofNullable(labels.get("object_instance")).map(Integer::parseInt).orElse(null);
        this.bacnetObject = new BacnetObject(objectType, objectInstance);
    }

    public Labels getLabels() {
        return labels;
    }

    public DeviceDataPointId getDataPointId() {
        return dataPointId;
    }

    public Integer getDeviceInstance() {
        return deviceInstance;
    }

    public BacnetObject getBacnetObject() {
        return bacnetObject;
    }

    public BacnetObjectProperties toBacnetObjectProperties() {
        return new BacnetObjectProperties(bacnetObject);
    }
}
