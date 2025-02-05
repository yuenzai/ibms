package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface DeviceDataAcquisitionRepository {
    Collection<Event> save(SaveDataAcquisitionCommand command);

    Collection<Event> remove(DeviceDataAcquisitionId dataAcquisitionId);

    Optional<DeviceDataAcquisition> get(DeviceDataAcquisitionId dataAcquisitionId);

    Page<DeviceDataAcquisition> search(Pageable pageable);
}
