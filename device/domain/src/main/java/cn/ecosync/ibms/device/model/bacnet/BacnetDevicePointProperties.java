package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.device.model.DevicePointProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
public class BacnetDevicePointProperties implements DevicePointProperties {
    @NotNull(message = "objectType must not be null")
    private Integer objectType;
    @NotNull(message = "objectId must not be null")
    private Integer objectId;
    @NotNull(message = "propertyId must not be null")
    private Integer propertyId;
}
