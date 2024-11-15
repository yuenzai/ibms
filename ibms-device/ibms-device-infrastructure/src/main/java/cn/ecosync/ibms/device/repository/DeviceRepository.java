package cn.ecosync.ibms.device.repository;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface DeviceRepository {
    void add(Device device);

    void remove(Device device);

    default void remove(DeviceId deviceId) {
        get(deviceId).ifPresent(this::remove);
    }

    Optional<Device> get(DeviceId deviceId);

    List<Device> findAll();

    Page<Device> findAll(Pageable pageable);
}
