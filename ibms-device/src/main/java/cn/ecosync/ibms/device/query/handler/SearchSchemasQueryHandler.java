package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.query.SearchSchemasQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchSchemasQueryHandler implements QueryHandler<SearchSchemasQuery, Page<DeviceSchemas>> {
    private final DeviceSchemasJpaRepository schemasRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceSchemas> handle(SearchSchemasQuery query) {
        Pageable pageable = query.toPageable();
        return schemasRepository.findAll(pageable)
                .map(DeviceSchemasEntity::getDeviceSchemas);
    }
}
