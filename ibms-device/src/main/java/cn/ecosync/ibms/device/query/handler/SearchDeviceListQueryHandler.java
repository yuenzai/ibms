package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.domain.DeviceReadonlyRepository;
import cn.ecosync.ibms.device.domain.DeviceRepository;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.query.ListSearchDeviceQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchDeviceListQueryHandler implements QueryHandler<ListSearchDeviceQuery, List<DeviceDto>> {
    private final DeviceRepository deviceRepository;
    private final DeviceReadonlyRepository deviceReadonlyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto> handle(ListSearchDeviceQuery query) {
        if (query.getReadonly()) {
            return deviceReadonlyRepository.findAll();
        } else {
            return deviceRepository.findAll().stream()
                    .map(DeviceMapper::map)
                    .collect(Collectors.toList());
        }
    }
}
