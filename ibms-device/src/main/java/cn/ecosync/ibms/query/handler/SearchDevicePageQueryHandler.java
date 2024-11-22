package cn.ecosync.ibms.query.handler;

import cn.ecosync.ibms.mapper.DeviceMapper;
import cn.ecosync.ibms.dto.DeviceDto;
import cn.ecosync.ibms.query.SearchDevicePageQuery;
import cn.ecosync.ibms.domain.DeviceReadonlyRepository;
import cn.ecosync.ibms.domain.DeviceRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDevicePageQueryHandler implements QueryHandler<SearchDevicePageQuery, Page<DeviceDto>> {
    private final DeviceRepository deviceRepository;
    private final DeviceReadonlyRepository deviceReadonlyRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDto> handle(SearchDevicePageQuery query) {
        if (query.getReadonly()) {
            return deviceReadonlyRepository.findAll(query.getPageable());
        } else {
            return deviceRepository.findAll(query.getPageable())
                    .map(DeviceMapper::map);
        }
    }
}
