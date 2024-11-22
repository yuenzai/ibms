package cn.ecosync.ibms.query;

import cn.ecosync.ibms.dto.DeviceDto;
import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
