package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DeviceDataAcquisitionJpaRepository extends JpaRepository<DeviceDataAcquisitionEntity, Integer> {
    Optional<DeviceDataAcquisitionEntity> findByDataAcquisitionId(DeviceDataAcquisitionId dataAcquisitionId);

    List<DeviceDataAcquisitionEntity> findByDataAcquisitionIdIn(Set<DeviceDataAcquisitionId> dataAcquisitionIds);

    void removeByDataAcquisitionId(DeviceDataAcquisitionId dataAcquisitionId);
}
