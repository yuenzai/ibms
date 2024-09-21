package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DevicePointProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DevicePointDto {
    private String pointCode;
    private String pointName;
    private DevicePointProperties pointProperties;
}
