package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.query.Query;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
public class GetDeviceQuery implements Query<Optional<DeviceDto>> {
    private final DeviceId deviceId;
    private final Boolean readonly;

    public GetDeviceQuery(String deviceCode, Boolean readonly) {
        this.deviceId = new DeviceId(deviceCode);
        this.readonly = readonly;
    }
}
