package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
public class SearchDeviceQuery implements Query<Iterable<DeviceDto>> {
    private final Integer page;
    private final Integer pageSize;
    private final Boolean readonly;

    public SearchDeviceQuery(Boolean readonly) {
        this(null, null, readonly);
    }

    public SearchDeviceQuery(Integer pageSize, Integer page, Boolean readonly) {
        this.pageSize = pageSize;
        this.page = page;
        this.readonly = readonly;
    }

    public Pageable toPageable() {
        return CollectionUtils.of(page, pageSize);
    }
}
