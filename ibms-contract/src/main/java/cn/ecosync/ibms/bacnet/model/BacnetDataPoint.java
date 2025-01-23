package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectProperties;
import cn.ecosync.ibms.device.model.DeviceDataPoint;
import cn.ecosync.ibms.device.model.DeviceDataPointId;
import cn.ecosync.ibms.device.model.DeviceDataPointLabel;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(callSuper = true)
public class BacnetDataPoint extends DeviceDataPoint {
    @NotNull
    private Integer deviceInstance;
    @Valid
    @JsonUnwrapped
    private BacnetObject bacnetObject;

    protected BacnetDataPoint() {
    }

    public BacnetDataPoint(DeviceDataPointId dataPointId, Integer deviceInstance, BacnetObject bacnetObject, List<DeviceDataPointLabel> labels) {
        super(dataPointId, labels);
        this.deviceInstance = deviceInstance;
        this.bacnetObject = bacnetObject;
    }

    public BacnetObjectProperties toBacnetObjectProperties() {
        return new BacnetObjectProperties(bacnetObject);
    }
}
