package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
@RequiredArgsConstructor
public class SearchDeviceQuery implements Query<Iterable<DeviceDto>> {
    private final Integer page;
    private final Integer pageSize;

    public Pageable toPageable() {
        return CollectionUtils.of(page, pageSize);
    }
}
