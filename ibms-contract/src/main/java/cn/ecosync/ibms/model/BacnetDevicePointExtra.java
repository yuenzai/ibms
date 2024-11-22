package cn.ecosync.ibms.model;

import cn.ecosync.ibms.dto.DevicePointExtra;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;

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
