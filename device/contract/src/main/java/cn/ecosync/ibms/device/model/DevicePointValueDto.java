package cn.ecosync.ibms.device.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DevicePointValueDto {
    private String pointCode;
    private DevicePointValue pointValue;
}
