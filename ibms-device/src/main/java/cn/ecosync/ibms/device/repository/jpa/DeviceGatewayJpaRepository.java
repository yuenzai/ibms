package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceGatewayJpaRepository extends JpaRepository<DeviceGatewayEntity, Integer> {
    Optional<DeviceGatewayEntity> findByGatewayId(DeviceGatewayId gatewayId);

    void removeByGatewayId(DeviceGatewayId gatewayId);
}
