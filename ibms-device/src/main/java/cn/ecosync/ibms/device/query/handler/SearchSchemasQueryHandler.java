package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.query.SearchSchemasQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchSchemasQueryHandler implements QueryHandler<SearchSchemasQuery, List<DeviceSchemas>> {
    private final DeviceSchemasJpaRepository schemasRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceSchemas> handle(SearchSchemasQuery query) {
        Pageable pageable = query.toPageable();
        List<DeviceSchemasEntity> schemasEntities = pageable.isPaged()
                ? schemasRepository.findAll(pageable).getContent()
                : schemasRepository.findAll(pageable.getSort());
        return schemasEntities.stream()
                .map(DeviceSchemasEntity::getSchemas)
                .collect(Collectors.toList());
    }
}
