package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.DeviceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceReadonlyJpaRepository extends JpaRepository<DeviceDto, Integer> {
    Optional<DeviceDto> findByDeviceId(DeviceId deviceId);
}
