package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceCommandModel;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

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
        return findAll(newExample(probe), sort).stream()
                .map(DeviceModel.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    default Page<DeviceModel> search(DeviceModel probe, Pageable pageable) {
        return findAll(newExample(probe), pageable)
                .map(DeviceModel.class::cast);
    }

    default Example<Device> newExample(DeviceModel model) {
        Device probe = Device.newProbe(model);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("daqId.daqCode", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("deviceId.deviceCode", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("deviceProperties.deviceName", ExampleMatcher.GenericPropertyMatcher::contains);
        return Example.of(probe, matcher);
    }

    Optional<Device> findByDeviceId(DeviceId deviceId);
}
