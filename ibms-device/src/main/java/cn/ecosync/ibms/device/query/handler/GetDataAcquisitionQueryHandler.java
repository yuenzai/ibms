package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.jpa.DeviceEntity;
import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.*;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetDataAcquisitionQueryHandler implements QueryHandler<GetDataAcquisitionQuery, DeviceDataAcquisition> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;
    private final DeviceSchemasJpaRepository deviceSchemasRepository;
    private final DeviceJpaRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public DeviceDataAcquisition handle(GetDataAcquisitionQuery query) {
        Pageable pageable = query.toPageable();
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(query.getDataAcquisitionCode());
        DeviceDataAcquisitionEntity dataAcquisitionEntity;
        DeviceSchemasId schemasId;
        DeviceSchemas schemas;
        Set<DeviceId> deviceIds;
        List<DeviceEntity> devicesEntities;
        List<Device> devices;

        dataAcquisitionEntity = dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity == null) return null;

        schemasId = dataAcquisitionEntity.getSchemas().getSchemasId();
        schemas = deviceSchemasRepository.findBySchemasId(schemasId)
                .map(DeviceSchemasEntity::getSchemas)
                .orElse(null);

        deviceIds = dataAcquisitionEntity.getDevices().stream()
                .map(Device::getDeviceId)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        devicesEntities = pageable.isPaged()
                ? deviceRepository.findByDeviceIdIn(deviceIds, pageable).getContent()
                : deviceRepository.findByDeviceIdIn(deviceIds);
        devices = devicesEntities.stream()
                .map(DeviceEntity::getDevice)
                .collect(Collectors.toList());

        return dataAcquisitionEntity.getDataAcquisition()
                .withSchemas(schemas)
                .withDevices(devices);
    }
}
