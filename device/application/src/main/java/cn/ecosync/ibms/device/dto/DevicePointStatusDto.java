package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointValue;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DevicePointStatusDto extends DevicePointDto {
    private DevicePointValue value;

    public DevicePointStatusDto() {
    }

    public DevicePointStatusDto(DevicePoint devicePoint, DevicePointValue value) {
        super(devicePoint);
        this.value = value;
    }
}
