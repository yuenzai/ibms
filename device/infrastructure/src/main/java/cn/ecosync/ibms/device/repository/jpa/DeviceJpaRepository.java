package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceJpaRepository extends JpaRepository<Device, Integer>, DeviceRepository {
    @Override
    default void add(Device device) {
        save(device);
    }

    @Override
    default void remove(Device device) {
        delete(device);
    }

    @Override
    default Optional<Device> get(DeviceId deviceId) {
        return findByDeviceId(deviceId);
    }

    Optional<Device> findByDeviceId(DeviceId deviceId);
}
