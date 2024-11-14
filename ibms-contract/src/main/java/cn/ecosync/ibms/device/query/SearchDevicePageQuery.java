package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static cn.ecosync.ibms.device.DeviceConstant.ENV_DEVICE_SERVICE_HOST;

@Getter
@ToString
public class SearchDevicePageQuery implements Query<Page<DeviceDto>> {
    private final Pageable pageable;
    private final Boolean readonly;

    public SearchDevicePageQuery(Integer page, Integer pageSize, Boolean readonly) {
        this(CollectionUtils.of(page, pageSize), readonly);
    }

    public SearchDevicePageQuery(Pageable pageable, Boolean readonly) {
        this.pageable = pageable;
        this.readonly = readonly;
    }

    @Override
    public HttpRequest httpRequest() {
        HttpRequest.Builder builder = HttpRequest.getMethod()
                .hostEnvironmentKey(ENV_DEVICE_SERVICE_HOST)
                .pathSegments("device")
                .queryParam("readonly", readonly)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("pagesize", pageable.getPageSize());
        return builder.build();
    }
}
