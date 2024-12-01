package cn.ecosync.ibms.device.model;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface DeviceCommandModelRepository {
    void add(DeviceCommandModel device);

    void remove(DeviceCommandModel device);

    Optional<DeviceCommandModel> get(DeviceId deviceId);

    default void remove(DeviceId deviceId) {
        get(deviceId).ifPresent(this::remove);
    }
}
