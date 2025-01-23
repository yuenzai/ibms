//package cn.ecosync.ibms.device.query.handler;
//
//import cn.ecosync.ibms.device.dto.DeviceDataAcquisitionView;
//import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
//import cn.ecosync.ibms.device.jpa.DeviceEntity;
//import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
//import cn.ecosync.ibms.device.model.*;
//import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
//import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
//import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
//import cn.ecosync.ibms.device.repository.jpa.DeviceSchemasJpaRepository;
//import cn.ecosync.iframework.query.QueryHandler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//
//@Component
//@RequiredArgsConstructor
//public class GetDataAcquisitionQueryHandlerBak implements QueryHandler<GetDataAcquisitionQuery, IDeviceDataAcquisition> {
//    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;
//    private final DeviceSchemasJpaRepository deviceSchemasRepository;
//    private final DeviceJpaRepository deviceRepository;
//
//    @Override
//    @Transactional(readOnly = true)
//    public IDeviceDataAcquisition handle(GetDataAcquisitionQuery query) {
//        Pageable pageable = query.toPageable();
//        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(query.getDataAcquisitionCode());
//        DeviceDataAcquisitionEntity dataAcquisitionEntity;
//        DeviceSchemasId schemasId;
//        DeviceSchemas schemas;
//        Page<Device> devices;
//
//        dataAcquisitionEntity = dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
//        if (dataAcquisitionEntity == null) return null;
//        DeviceDataAcquisition dataAcquisition = dataAcquisitionEntity.getDataAcquisition();
//        schemasId = dataAcquisitionEntity.getSchemas().getSchemasId();
//        schemas = deviceSchemasRepository.findBySchemasId(schemasId)
//                .map(DeviceSchemasEntity::getDeviceSchemas)
//                .orElse(dataAcquisition.getSchemas());
//        devices = deviceRepository.findByDataAcquisitionEntities(Collections.singleton(dataAcquisitionEntity), pageable)
//                .map(DeviceEntity::getDevice);
//
//        if (pageable.isPaged()) {
//            return new DeviceDataAcquisitionView(dataAcquisition.getDataAcquisitionId(), dataAcquisition.getScrapeInterval(), schemas, devices);
//        } else {
//            return dataAcquisition
//                    .withSchemas(schemas)
//                    .withDevices(devices.getContent());
//        }
//    }
//}
