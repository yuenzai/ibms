package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DevicePointDto;
import cn.ecosync.ibms.device.model.DeviceId;
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

    public DeviceId toDeviceId() {
        return new DeviceId(this.deviceCode);
    }
}
