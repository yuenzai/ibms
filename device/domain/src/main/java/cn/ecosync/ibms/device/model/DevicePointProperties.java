package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.model.bacnet.BacnetDevicePointProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetDevicePointProperties.class, name = "BACnet"),
})
public interface DevicePointProperties {
}
