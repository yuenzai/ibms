package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.dto.BacnetDeviceExtra;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetDeviceExtra.class, name = "BACNET"),
})
public interface DeviceExtra {
}
