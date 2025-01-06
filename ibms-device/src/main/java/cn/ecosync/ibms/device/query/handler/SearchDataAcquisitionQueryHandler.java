package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchDataAcquisitionQueryHandler implements QueryHandler<SearchDataAcquisitionQuery, List<DeviceDataAcquisition>> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;
    private final DeviceSchemasJpaRepository schemasRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDataAcquisition> handle(SearchDataAcquisitionQuery query) {
        Pageable pageable = query.toPageable();
        List<DeviceDataAcquisitionEntity> dataAcquisitionEntities;
        Set<DeviceSchemasId> schemasIds;
        Map<DeviceSchemasId, DeviceSchemas> schemasEntities;

        dataAcquisitionEntities = pageable.isPaged()
                ? dataAcquisitionRepository.findAll(pageable).getContent()
                : dataAcquisitionRepository.findAll(pageable.getSort());
        schemasIds = dataAcquisitionEntities.stream()
                .map(DeviceDataAcquisitionEntity::getSchemas)
                .map(DeviceSchemas::getSchemasId)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        schemasEntities = schemasRepository.findBySchemasIdIn(schemasIds).stream()
                .map(DeviceSchemasEntity::getSchemas)
                .collect(Collectors.toMap(DeviceSchemas::getSchemasId, Function.identity()));

        List<DeviceDataAcquisition> dataAcquisitions = new ArrayList<>(dataAcquisitionEntities.size());
        for (DeviceDataAcquisitionEntity dataAcquisitionEntity : dataAcquisitionEntities) {
            DeviceDataAcquisition dataAcquisition = dataAcquisitionEntity.getDataAcquisition();
            DeviceSchemasId schemasId = dataAcquisition.getSchemas().getSchemasId();
            DeviceSchemas deviceSchemas = schemasEntities.get(schemasId);
            dataAcquisitions.add(dataAcquisition.withSchemas(deviceSchemas));
        }
        return dataAcquisitions;
    }
}
