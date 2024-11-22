package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.bacnet.model.BacnetDevicePointExtra;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetDevicePointExtra.class, name = "BACNET"),
})
public interface DevicePointExtra {
}
