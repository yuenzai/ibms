package cn.ecosync.ibms.device.repository;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionCommandModel;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface DeviceDataAcquisitionRepository {
    void add(DeviceDataAcquisitionCommandModel daq);

    void remove(DeviceDataAcquisitionCommandModel daq);

    Optional<DeviceDataAcquisitionCommandModel> get(DeviceDataAcquisitionId daqId);

    List<DeviceDataAcquisitionModel> search(DeviceDataAcquisitionModel probe, Sort sort);

    Page<DeviceDataAcquisitionModel> search(DeviceDataAcquisitionModel probe, Pageable pageable);
}
