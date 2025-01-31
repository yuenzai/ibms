package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeviceGatewayJpaRepository extends JpaRepository<DeviceGatewayEntity, Integer> {
    Optional<DeviceGatewayEntity> findByGatewayId(DeviceGatewayId gatewayId);

    @Query("select gateway from DeviceGatewayEntity gateway where gateway.gatewayId = :gatewayId and gateway.synchronizationState = :desiredState")
    Optional<DeviceGatewayEntity> findByGatewayId(DeviceGatewayId gatewayId, String desiredState);

    void removeByGatewayId(DeviceGatewayId gatewayId);
}
