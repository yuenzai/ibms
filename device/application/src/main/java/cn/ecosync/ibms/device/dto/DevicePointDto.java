package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.model.DevicePointProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.Objects;

@Getter
@ToString
public class DevicePointDto {
    @Valid
    @JsonUnwrapped
    private DevicePointId pointId;
    @Valid
    @JsonUnwrapped
    private DevicePointProperties pointProperties;

    protected DevicePointDto() {
    }

    public DevicePointDto(DevicePointId pointId, DevicePointProperties pointProperties) {
        this.pointId = pointId;
        this.pointProperties = pointProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DevicePointDto)) return false;
        DevicePointDto that = (DevicePointDto) o;
        return Objects.equals(pointId, that.pointId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pointId);
    }
}
