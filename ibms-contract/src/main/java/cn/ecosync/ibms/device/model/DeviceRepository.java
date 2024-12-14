package cn.ecosync.ibms.device.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface DeviceRepository {
    void add(DeviceCommandModel device);

    void remove(DeviceCommandModel device);

    Optional<DeviceCommandModel> get(DeviceId deviceId);

    List<DeviceModel> search(DeviceModel probe, Sort sort);

    Page<DeviceModel> search(DeviceModel probe, Pageable pageable);

    DeviceModel newProbe(DeviceDataAcquisitionId daqId, String deviceName, String path);
}
