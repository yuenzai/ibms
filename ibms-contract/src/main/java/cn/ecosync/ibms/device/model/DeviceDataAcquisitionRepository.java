package cn.ecosync.ibms.device.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface DeviceDataAcquisitionRepository {
    void add(DeviceDataAcquisitionCommandModel daq);

    void remove(DeviceDataAcquisitionCommandModel daq);

    Optional<DeviceDataAcquisitionCommandModel> get(DeviceDataAcquisitionId daqId);

    List<DeviceDataAcquisitionModel> search(DeviceDataAcquisitionModel probe, Sort sort);

    Page<DeviceDataAcquisitionModel> search(DeviceDataAcquisitionModel probe, Pageable pageable);

    DeviceDataAcquisitionModel newProbe();
}
