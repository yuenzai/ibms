package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceDataAcquisitionDao extends JpaRepository<DeviceDataAcquisitionEntity, Integer> {
    Optional<DeviceDataAcquisitionEntity> findByDataAcquisitionId(DeviceDataAcquisitionId dataAcquisitionId);
}
