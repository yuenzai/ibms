package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DeviceStatusDto;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.query.Query;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Getter
@ToString
public class GetDeviceStatusQuery implements Query<Optional<DeviceStatusDto>> {
    @NotBlank
    private String deviceCode;

    protected GetDeviceStatusQuery() {
    }

    public GetDeviceStatusQuery(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public DeviceId toDeviceId() {
        return new DeviceId(this.deviceCode);
    }
}
