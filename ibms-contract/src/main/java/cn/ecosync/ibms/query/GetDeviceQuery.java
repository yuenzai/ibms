package cn.ecosync.ibms.query;

import cn.ecosync.ibms.dto.DeviceDto;
import cn.ecosync.iframework.query.Query;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetDeviceQuery implements Query<DeviceDto> {
    private final String deviceCode;
    private final Boolean readonly;

    public GetDeviceQuery(String deviceCode, Boolean readonly) {
        this.deviceCode = deviceCode;
        this.readonly = readonly;
    }
}
