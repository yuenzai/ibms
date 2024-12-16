package cn.ecosync.ibms.device.repository;

import cn.ecosync.ibms.device.model.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceJpaRepository extends JpaRepository<Device, Integer>, DeviceRepository<Device> {
    @Override
    default void add(Device device) {
        save(device);
    }

    @Override
    default void remove(Device device) {
        delete(device);
    }

    @Override
    default Optional<DeviceCommandModel> get(DeviceId deviceId) {
        return findByDeviceId(deviceId)
                .map(DeviceCommandModel.class::cast);
    }

    @Override
    default List<Device> search(Example<Device> example, Sort sort) {
        return findAll(example, sort);
    }

    @Override
    default Page<Device> search(Example<Device> example, Pageable pageable) {
        return findAll(example, pageable);
    }

    @Override
    default Example<Device> newExample(DeviceId deviceIdProbe, DeviceDataAcquisitionId daqIdProbe, DeviceProperties devicePropertiesProbe) {
        Device probe = Device.newProbe(deviceIdProbe, daqIdProbe, devicePropertiesProbe);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("daqId.daqCode", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("deviceId.deviceCode", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("deviceProperties.deviceName", ExampleMatcher.GenericPropertyMatcher::contains);
        return Example.of(probe, matcher);
    }

    Optional<Device> findByDeviceId(DeviceId deviceId);
}
