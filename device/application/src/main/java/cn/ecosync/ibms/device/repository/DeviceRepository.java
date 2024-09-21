package cn.ecosync.ibms.device.repository;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface DeviceRepository {
    void add(Device device);

    void remove(Device device);

    default void remove(DeviceId deviceId) {
        get(deviceId).ifPresent(this::remove);
    }

    Optional<Device> get(DeviceId deviceId);
}
