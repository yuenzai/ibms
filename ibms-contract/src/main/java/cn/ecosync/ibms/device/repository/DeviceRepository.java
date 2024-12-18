package cn.ecosync.ibms.device.repository;

import cn.ecosync.ibms.device.model.DeviceCommandModel;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    void add(DeviceCommandModel device);

    void remove(DeviceCommandModel device);

    Optional<DeviceCommandModel> get(DeviceId deviceId);

    List<DeviceModel> search(DeviceModel probe, Sort sort);

    Page<DeviceModel> search(DeviceModel probe, Pageable pageable);
}
