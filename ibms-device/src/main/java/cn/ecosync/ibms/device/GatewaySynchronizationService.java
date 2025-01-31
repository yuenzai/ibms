package cn.ecosync.ibms.device;

import cn.ecosync.ibms.AbstractSynchronizationService;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.*;
import cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum;
import cn.ecosync.ibms.device.query.GetGatewayQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatewaySynchronizationService extends AbstractSynchronizationService<DeviceGatewayId, DeviceGateway, GetGatewayQuery> {
    private final Map<DeviceGatewayId, Consumer<DeviceGateway>> consumerMap = new ConcurrentHashMap<>();
    private final DeviceGatewayJpaRepository gatewayRepository;
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional(readOnly = true)
    public DeviceGateway handle(GetGatewayQuery query) {
        DeviceGatewayId gatewayId = query.getGatewayId();
        SynchronizationStateEnum desiredState = query.getDesiredState();
        DeviceGateway gateway = (desiredState != null ? gatewayRepository.findByGatewayId(gatewayId, desiredState.toString()) : gatewayRepository.findByGatewayId(gatewayId))
                .map(DeviceGatewayEntity::getGateway)
                .orElse(null);
        if (gateway == null) return null;
        List<DeviceDataAcquisition> dataAcquisitions = gateway.getDataAcquisitions().stream()
                .map(DeviceDataAcquisition::getDataAcquisitionId)
                .map(this::getDataAcquisition)
                .map(DeviceDataAcquisition.class::cast)
                .collect(Collectors.toList());
        return gateway.withDataAcquisitions(dataAcquisitions);
    }

    private IDeviceDataAcquisition getDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId) {
        return dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId)
                .map(DeviceDataAcquisitionEntity::getDataAcquisition)
                .orElse(null);
    }

    @Override
    public void put(DeviceGatewayId key, Consumer<DeviceGateway> value) {
        consumerMap.put(key, value);
    }

    @Override
    public Consumer<DeviceGateway> remove(DeviceGatewayId key) {
        return consumerMap.remove(key);
    }
}
