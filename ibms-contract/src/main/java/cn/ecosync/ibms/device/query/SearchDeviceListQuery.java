package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static cn.ecosync.ibms.device.DeviceConstant.ENV_DEVICE_SERVICE_HOST;

@Getter
@ToString
public class SearchDeviceListQuery implements Query<List<DeviceDto>> {
    private final Boolean readonly;

    public SearchDeviceListQuery(Boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public HttpRequest httpRequest() {
        HttpRequest.Builder builder = HttpRequest.getMethod()
                .hostEnvironmentKey(ENV_DEVICE_SERVICE_HOST)
                .pathSegments("device")
                .queryParam("readonly", this.readonly);
        return builder.build();
    }
}
