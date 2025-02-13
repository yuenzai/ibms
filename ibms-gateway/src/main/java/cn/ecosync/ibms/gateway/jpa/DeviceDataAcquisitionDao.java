package cn.ecosync.ibms.gateway.jpa;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceDataAcquisitionDao extends JpaRepository<DeviceDataAcquisitionEntity, Integer> {
    Optional<DeviceDataAcquisitionEntity> findByDataAcquisitionId(DeviceDataAcquisitionId dataAcquisitionId);
}
