package cn.ecosync.aiot.edge.gateway.model;

import cn.ecosync.aiot.edge.gateway.command.SaveDataAcquisitionCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeviceDataAcquisitionRepository {
    void save(SaveDataAcquisitionCommand command);

    void remove(DeviceDataAcquisitionId dataAcquisitionId);

    Optional<DeviceDataAcquisition> get(DeviceDataAcquisitionId dataAcquisitionId);

    Page<DeviceDataAcquisition> search(Pageable pageable);
}
