package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequestProperties;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static cn.ecosync.ibms.device.DeviceConstant.ENVIRONMENT_DEVICE_SERVICE_URL;

@Getter
@ToString
public class SearchDeviceListQuery implements Query<List<DeviceDto>> {
    private final Boolean readonly;

    public SearchDeviceListQuery(Boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public HttpRequestProperties httpRequestProperties() {
        HttpRequestProperties.Builder builder = HttpRequestProperties.builder()
                .hostEnvironmentKey(ENVIRONMENT_DEVICE_SERVICE_URL)
                .pathSegments("device")
                .queryParam("readonly", this.readonly);
        return builder.build();
    }
}
