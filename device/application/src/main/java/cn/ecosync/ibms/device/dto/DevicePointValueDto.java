package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.model.DevicePointValue;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DevicePointValueDto {
    private DevicePointId pointId;
    private DevicePointValue pointValue;
}
