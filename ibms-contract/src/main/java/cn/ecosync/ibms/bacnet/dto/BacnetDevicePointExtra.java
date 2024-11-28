package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.ibms.device.dto.DevicePointExtra;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetDevicePointExtra implements DevicePointExtra {
    @Valid
    @JsonUnwrapped
    private BacnetObjectProperty bacnetObjectProperty;

    public BacnetObject toBacnetObject() {
        return bacnetObjectProperty.toBacnetObject();
    }

    public BacnetProperty toBacnetProperty() {
        return bacnetObjectProperty.toBacnetProperty();
    }
}
