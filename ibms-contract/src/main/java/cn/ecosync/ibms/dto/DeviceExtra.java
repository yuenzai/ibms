package cn.ecosync.ibms.dto;

import cn.ecosync.ibms.model.BacnetDeviceExtra;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetDeviceExtra.class, name = "BACNET"),
})
public interface DeviceExtra {
}
