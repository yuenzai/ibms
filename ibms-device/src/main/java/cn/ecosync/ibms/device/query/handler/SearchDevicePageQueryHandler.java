package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.domain.DeviceReadonlyRepository;
import cn.ecosync.ibms.device.domain.DeviceRepository;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.query.PageSearchDeviceQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDevicePageQueryHandler implements QueryHandler<PageSearchDeviceQuery, Page<DeviceDto>> {
    private final DeviceRepository deviceRepository;
    private final DeviceReadonlyRepository deviceReadonlyRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDto> handle(PageSearchDeviceQuery query) {
        Pageable pageable = query.toPageable();
        if (query.getReadonly()) {
            return deviceReadonlyRepository.findAll(pageable);
        } else {
            return deviceRepository.findAll(pageable)
                    .map(DeviceMapper::map);
        }
    }
}
