package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.AddDeviceCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.jpa.DeviceEntity;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddDeviceCommandHandler implements CommandHandler<AddDeviceCommand> {
    private final DeviceJpaRepository deviceRepository;
    private final DeviceSchemasJpaRepository schemasRepository;

    @Override
    @Transactional
    public void handle(AddDeviceCommand command) {
        DeviceSchemasId schemasId = new DeviceSchemasId(command.getSchemasCode());
        Collection<? extends Device> devices = command.toDevices();
        DeviceSchemasEntity schemas;
        Set<DeviceId> deviceIds;

        schemas = schemasRepository.findBySchemasId(schemasId).orElse(null);
        Assert.notNull(schemas, "Schemas not exists");
        deviceIds = devices.stream()
                .map(Device::getDeviceId)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        List<DeviceEntity> existsDevices = deviceRepository.findByDeviceIdIn(deviceIds);
        Assert.isTrue(CollectionUtils.isEmpty(existsDevices), "Device already exists: " + existsDevices);

        List<DeviceEntity> deviceEntities = devices.stream()
                .map(DeviceEntity::new)
                .collect(Collectors.toList());
        deviceRepository.saveAll(deviceEntities);
    }
}
