package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.Getter;
import lombok.ToString;

import static cn.ecosync.ibms.device.DeviceConstant.ENV_DEVICE_SERVICE_HOST;

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
    public HttpRequest httpRequest() {
        return HttpRequest.getMethod()
                .hostEnvironmentKey(ENV_DEVICE_SERVICE_HOST)
                .pathSegments("device", this.deviceCode)
                .queryParam("readonly", this.readonly)
                .build();
    }
}
