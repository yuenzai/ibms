package cn.ecosync.ibms.gateway.model;

import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
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
