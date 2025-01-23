package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetDataPoint.class, name = "BACNET"))
public abstract class DeviceDataPoint {
    @Valid
    @JsonUnwrapped
    private DeviceDataPointId dataPointId;
    private List<@Valid DeviceDataPointLabel> labels;

    protected DeviceDataPoint() {
    }

    public DeviceDataPoint(DeviceDataPointId dataPointId, List<DeviceDataPointLabel> labels) {
        this.dataPointId = dataPointId;
        this.labels = labels;
    }

    public List<DeviceDataPointLabel> getLabels() {
        return Collections.unmodifiableList(CollectionUtils.nullSafeOf(labels));
    }
}
