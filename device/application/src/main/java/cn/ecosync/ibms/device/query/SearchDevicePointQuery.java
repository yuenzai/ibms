package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DevicePointDto;
import cn.ecosync.ibms.query.Query;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SearchDevicePointQuery implements Query<Iterable<DevicePointDto>> {
    private String deviceCode;

    protected SearchDevicePointQuery() {
    }

    public SearchDevicePointQuery(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
