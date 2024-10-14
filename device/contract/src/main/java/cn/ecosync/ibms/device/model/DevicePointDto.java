package cn.ecosync.ibms.device.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DevicePointDto {
    private String pointCode;
    private String pointName;
    private DevicePointExtra pointExtra;

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
