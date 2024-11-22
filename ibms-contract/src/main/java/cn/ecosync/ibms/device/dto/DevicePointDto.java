package cn.ecosync.ibms.device.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class DevicePointDto {
    private String pointCode;
    private String pointName;
    private DevicePointExtra pointExtra;

    protected DevicePointDto() {
    }

    public DevicePointDto(String pointCode, String pointName, DevicePointExtra pointExtra) {
        this.pointCode = pointCode;
        this.pointName = pointName;
        this.pointExtra = pointExtra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DevicePointDto)) return false;
        DevicePointDto that = (DevicePointDto) o;
        return Objects.equals(pointCode, that.pointCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pointCode);
    }
}
