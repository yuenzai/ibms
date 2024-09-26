package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.model.DevicePointProperties;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public class DevicePointDto {
    private String deviceCode;
    private String pointCode;
    private String pointName;
    private DevicePointProperties pointProperties;

    public DevicePointDto() {
    }

    public DevicePointDto(DevicePoint devicePoint) {
        DevicePointId pointId = devicePoint.getPointId();
        this.deviceCode = pointId.getDeviceCode();
        this.pointCode = pointId.getPointCode();
        this.pointName = devicePoint.getPointName();
        this.pointProperties = devicePoint.getPointProperties();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DevicePointDto)) return false;
        DevicePointDto that = (DevicePointDto) o;
        return Objects.equals(deviceCode, that.deviceCode) && Objects.equals(pointCode, that.pointCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceCode, pointCode);
    }
}
