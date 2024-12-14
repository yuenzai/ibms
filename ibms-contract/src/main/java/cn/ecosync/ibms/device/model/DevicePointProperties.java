package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.dto.BacnetDevicePointExtra;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "pointType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetDevicePointExtra.class, name = "BACNET"),
})
public interface DevicePointProperties {
}
