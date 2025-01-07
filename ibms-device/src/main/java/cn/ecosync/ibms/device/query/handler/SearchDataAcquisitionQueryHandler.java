package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchDataAcquisitionQueryHandler implements QueryHandler<SearchDataAcquisitionQuery, Page<DeviceDataAcquisition>> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;
    private final DeviceSchemasJpaRepository schemasRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDataAcquisition> handle(SearchDataAcquisitionQuery query) {
        Page<DeviceDataAcquisition> dataAcquisitions;
        Set<DeviceSchemasId> schemasIds;
        Map<DeviceSchemasId, DeviceSchemas> schemasMap;

        dataAcquisitions = dataAcquisitionRepository.findAll(query.toPageable())
                .map(DeviceDataAcquisitionEntity::getDataAcquisition);
        schemasIds = dataAcquisitions.stream()
                .map(DeviceDataAcquisition::getSchemas)
                .map(DeviceSchemas::getSchemasId)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        schemasMap = schemasRepository.findBySchemasIdIn(schemasIds).stream()
                .map(DeviceSchemasEntity::getSchemas)
                .collect(Collectors.toMap(DeviceSchemas::getSchemasId, Function.identity()));
        return dataAcquisitions.map(in -> {
            DeviceSchemasId schemasId = in.getSchemas().getSchemasId();
            DeviceSchemas deviceSchemas = schemasMap.get(schemasId);
            return in.withSchemas(deviceSchemas);
        });
    }
}
