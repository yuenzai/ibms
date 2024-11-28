package cn.ecosync.ibms.model;

import cn.ecosync.ibms.dto.DeviceExtra;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetDeviceExtra implements DeviceExtra {
    @NotNull
    private Integer deviceInstance;
}
