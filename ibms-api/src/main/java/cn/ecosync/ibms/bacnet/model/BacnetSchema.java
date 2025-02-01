package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.bacnet.dto.BacnetObjectProperties;
import cn.ecosync.ibms.device.dto.DeviceSchema;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class BacnetSchema extends DeviceSchema {
    @Valid
    @JsonUnwrapped
    private BacnetObjectProperties schemaProperties;
}
