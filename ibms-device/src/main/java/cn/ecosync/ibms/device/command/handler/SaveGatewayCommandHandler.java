package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.SaveGatewayCommand;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SaveGatewayCommandHandler implements CommandHandler<SaveGatewayCommand> {
    private final DeviceGatewayJpaRepository gatewayRepository;
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional
    public void handle(SaveGatewayCommand command) {
        DeviceGatewayId gatewayId = new DeviceGatewayId(command.getGatewayCode());
        DeviceGatewayEntity gatewayEntity = gatewayRepository.findByGatewayId(gatewayId).orElse(null);
        Assert.notNull(gatewayEntity, "Gateway not exists");

        Set<DeviceDataAcquisitionId> dataAcquisitionIds = command.getDataAcquisitionCodes().stream()
                .map(DeviceDataAcquisitionId::new)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        if (CollectionUtils.isEmpty(dataAcquisitionIds)) return;
        List<DeviceDataAcquisition> dataAcquisitionReferences = dataAcquisitionRepository.findByDataAcquisitionIdIn(dataAcquisitionIds).stream()
                .map(DeviceDataAcquisitionEntity::getDataAcquisition)
                .map(DeviceDataAcquisition::toReference)
                .collect(Collectors.toList());
        DeviceGateway gateway = new DeviceGateway(gatewayId, dataAcquisitionReferences);
        gatewayEntity.save(gateway);
    }
}
