package cn.ecosync.ibms.device.repository;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceCommandModel;
import cn.ecosync.ibms.device.model.DeviceCommandModelRepository;
import cn.ecosync.ibms.device.model.DeviceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

import java.util.Optional;

public interface DeviceCommandModelJpaRepository extends JpaRepository<Device, Integer>, DeviceCommandModelRepository {
    @Override
    default void add(DeviceCommandModel device) {
        Assert.isInstanceOf(Device.class, device, "DeviceCommandModel must be instance of Device");
        save((Device) device);
    }

    @Override
    default void remove(DeviceCommandModel device) {
        Assert.isInstanceOf(Device.class, device, "DeviceCommandModel must be instance of Device");
        delete((Device) device);
    }

    @Override
    default Optional<DeviceCommandModel> get(DeviceId deviceId) {
        return findByDeviceId(deviceId)
                .map(DeviceCommandModel.class::cast);
    }

    Optional<Device> findByDeviceId(DeviceId deviceId);
}
