package cn.ecosync.ibms.device.repository;

import cn.ecosync.ibms.device.model.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface DeviceJpaRepository extends JpaRepository<Device, Integer>, DeviceRepository {
    @Override
    default void add(DeviceCommandModel device) {
        save((Device) device);
    }

    @Override
    default void remove(DeviceCommandModel device) {
        delete((Device) device);
    }

    @Override
    default Optional<DeviceCommandModel> get(DeviceId deviceId) {
        return findByDeviceId(deviceId)
                .map(DeviceCommandModel.class::cast);
    }

    @Override
    default List<DeviceModel> search(DeviceModel probe, Sort sort) {
        Assert.isInstanceOf(Device.class, probe);
        Example<Device> example = Example.of((Device) probe);
        return findAll(example, sort).stream()
                .map(DeviceModel.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    default Page<DeviceModel> search(DeviceModel probe, Pageable pageable) {
        Assert.isInstanceOf(Device.class, probe);
        Example<Device> example = Example.of((Device) probe);
        return findAll(example, pageable)
                .map(DeviceModel.class::cast);
    }

    @Override
    default DeviceModel newProbe(DeviceDataAcquisitionId daqId, String deviceName, String path) {
        return Device.newProbe(daqId, deviceName, path);
    }

    Optional<Device> findByDeviceId(DeviceId deviceId);
}
