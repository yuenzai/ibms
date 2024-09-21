package cn.ecosync.ibms.device.repository.memory;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class DeviceMemoryRepository implements DeviceRepository {
    @Override
    public void add(Device device) {

    }

    @Override
    public void remove(Device device) {

    }

    @Override
    public Optional<Device> get(DeviceId deviceId) {
        return Optional.empty();
    }
}
