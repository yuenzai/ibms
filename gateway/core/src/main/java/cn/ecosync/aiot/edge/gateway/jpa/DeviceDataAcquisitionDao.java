package cn.ecosync.aiot.edge.gateway.jpa;

import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisitionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceDataAcquisitionDao extends JpaRepository<DeviceDataAcquisitionEntity, Integer> {
    Optional<DeviceDataAcquisitionEntity> findByDataAcquisitionId(DeviceDataAcquisitionId dataAcquisitionId);
}
