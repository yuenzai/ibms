package cn.ecosync.ibms.device.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeviceDataAcquisitionRepository {
    void save(DeviceDataAcquisition dataAcquisition);

    void remove(DeviceDataAcquisitionId dataAcquisitionId);

    Optional<DeviceDataAcquisition> get(DeviceDataAcquisitionId dataAcquisitionId);

    Page<DeviceDataAcquisition> search(Pageable pageable);
}
