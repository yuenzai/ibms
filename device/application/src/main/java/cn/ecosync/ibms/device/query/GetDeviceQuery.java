package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.query.Query;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Getter
@ToString
public class GetDeviceQuery implements Query<Optional<DeviceDto>> {
    @NotBlank
    private String deviceCode;

    protected GetDeviceQuery() {
    }

    public GetDeviceQuery(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
