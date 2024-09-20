package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.model.DevicePointProperties;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class DevicePointDto<T extends DevicePointProperties> {
    @NotBlank
    private String pointCode;
    private String pointName;
    @Valid
    @NotNull
    private T pointProperties;

    public DevicePointDto() {
    }

    public DevicePointId toDevicePointId() {
        return new DevicePointId(pointCode);
    }
}
