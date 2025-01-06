package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.UpdateDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.jpa.DeviceEntity;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
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
public class UpdateDataAcquisitionCommandHandler implements CommandHandler<UpdateDataAcquisitionCommand> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;
    private final DeviceJpaRepository deviceRepository;

    @Override
    @Transactional
    public void handle(UpdateDataAcquisitionCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(command.getDataAcquisitionCode());
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        Assert.notNull(dataAcquisitionEntity, "DataAcquisitionEntity not exists");

        UpdateDataAcquisitionCommand.Devices updateDeviceParams = command.getDevices().orElse(null);
        if (updateDeviceParams == null) return;
        Set<DeviceId> deviceIds = updateDeviceParams.getDeviceCodes().stream()
                .map(DeviceId::new)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        if (CollectionUtils.isEmpty(deviceIds)) return;
        List<Device> devices = deviceRepository.findByDeviceIdIn(deviceIds).stream()
                .map(DeviceEntity::getDevice)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(devices)) return;

        DeviceDataAcquisition dataAcquisition = dataAcquisitionEntity.getDataAcquisition();
        switch (updateDeviceParams.getOperation()) {
            case ADD:
                dataAcquisitionEntity.save(dataAcquisition.addDeviceReferences(devices));
                break;
            case REMOVE:
                dataAcquisitionEntity.save(dataAcquisition.removeDeviceReferences(devices));
                break;
        }
    }
}
