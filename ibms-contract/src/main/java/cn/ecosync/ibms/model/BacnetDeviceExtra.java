package cn.ecosync.ibms.model;

import cn.ecosync.ibms.dto.DeviceExtra;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class BacnetDeviceExtra implements DeviceExtra {
    @NotNull
    private Integer deviceInstance;
}
