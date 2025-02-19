package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectType;
import cn.ecosync.ibms.gateway.model.DeviceDataPointId;
import cn.ecosync.ibms.gateway.model.LabelValues;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.Constants.LABEL_DEVICE_CODE;
import static cn.ecosync.ibms.Constants.LABEL_METRIC_NAME;

@ToString(callSuper = true)
public class BacnetDataPoint extends LabelValues {
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

    public BacnetDataPoint(Map<Integer, String> head, List<String> labelValues) {
        super(labelValues);
        Map<String, Integer> reversedHead = head.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        String deviceCode = Optional.ofNullable(reversedHead.get(LABEL_DEVICE_CODE))
                .map(labelValues::get)
                .orElse(null);
        String metricName = Optional.ofNullable(reversedHead.get(LABEL_METRIC_NAME))
                .map(labelValues::get)
                .orElse(null);
        this.dataPointId = new DeviceDataPointId(metricName, deviceCode);
        this.deviceInstance = Optional.ofNullable(reversedHead.get("device_instance"))
                .map(labelValues::get)
                .map(Integer::parseInt)
                .orElse(null);
        BacnetObjectType objectType = Optional.ofNullable(reversedHead.get("object_type"))
                .map(labelValues::get)
                .map(BacnetObjectType::valueOf)
                .orElse(null);
        Integer objectInstance = Optional.ofNullable(reversedHead.get("object_instance"))
                .map(labelValues::get)
                .map(Integer::parseInt)
                .orElse(null);
        this.bacnetObject = new BacnetObject(objectType, objectInstance);
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
