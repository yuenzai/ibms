package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionDeleted;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.repository.DeviceDataAcquisitionDeletedRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceDataAcquisitionDeletedJpaRepository extends JpaRepository<DeviceDataAcquisitionDeleted, Integer>, DeviceDataAcquisitionDeletedRepository {
    @Override
    default void add(DeviceDataAcquisitionModel daq) {
        DeviceDataAcquisitionDeleted daqDeleted = new DeviceDataAcquisitionDeleted(daq);
        save(daqDeleted);
    }
}
