package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpUrlProperties;
import lombok.Getter;
import lombok.ToString;

import static cn.ecosync.ibms.device.DeviceConstant.ENVIRONMENT_DEVICE_SERVICE_URL;

@Getter
@ToString
public class GetDeviceQuery implements Query<DeviceDto> {
    private final String deviceCode;
    private final Boolean readonly;

    public GetDeviceQuery(String deviceCode, Boolean readonly) {
        this.deviceCode = deviceCode;
        this.readonly = readonly;
    }

    @Override
    public HttpUrlProperties httpUrlProperties() {
        return HttpUrlProperties.builder()
                .hostEnvironmentKey(ENVIRONMENT_DEVICE_SERVICE_URL)
                .pathSegments("device", this.deviceCode)
                .queryParam("readonly", this.readonly)
                .build();
    }
}
