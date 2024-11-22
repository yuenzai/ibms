package cn.ecosync.ibms.repository.jpa;

import cn.ecosync.ibms.domain.Device;
import cn.ecosync.ibms.domain.DeviceId;
import cn.ecosync.ibms.domain.DeviceRepository;
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
