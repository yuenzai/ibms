package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.HttpUrlProperties;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static cn.ecosync.ibms.device.DeviceConstant.ENVIRONMENT_DEVICE_SERVICE_URL;

@Getter
@ToString
public class SearchDevicePageQuery implements Query<Page<DeviceDto>> {
    private final Pageable pageable;
    private final Boolean readonly;

    public SearchDevicePageQuery(Integer pageSize, Integer page, Boolean readonly) {
        this(CollectionUtils.of(page, pageSize), readonly);
    }

    public SearchDevicePageQuery(Pageable pageable, Boolean readonly) {
        this.pageable = pageable;
        this.readonly = readonly;
    }

    @Override
    public HttpUrlProperties httpUrlProperties() {
        HttpUrlProperties.Builder builder = HttpUrlProperties.builder()
                .hostEnvironmentKey(ENVIRONMENT_DEVICE_SERVICE_URL)
                .pathSegments("device")
                .queryParam("readonly", this.readonly)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("pagesize", pageable.getPageSize());
        return builder.build();
    }
}