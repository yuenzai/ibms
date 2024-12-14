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

public interface DeviceDataAcquisitionJpaRepository extends JpaRepository<DeviceDataAcquisition, Integer>, DeviceDataAcquisitionRepository {
    @Override
    default void add(DeviceDataAcquisitionCommandModel daq) {
        save((DeviceDataAcquisition) daq);
    }

    @Override
    default void remove(DeviceDataAcquisitionCommandModel daq) {
        delete((DeviceDataAcquisition) daq);
    }

    @Override
    default Optional<DeviceDataAcquisitionCommandModel> get(DeviceDataAcquisitionId daqId) {
        return findByDaqId(daqId)
                .map(DeviceDataAcquisitionCommandModel.class::cast);
    }

    @Override
    default List<DeviceDataAcquisitionModel> search(DeviceDataAcquisitionModel probe, Sort sort) {
        Assert.isInstanceOf(DeviceDataAcquisition.class, probe);
        Example<DeviceDataAcquisition> example = Example.of((DeviceDataAcquisition) probe);
        return findAll(example, sort).stream()
                .map(DeviceDataAcquisitionModel.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    default Page<DeviceDataAcquisitionModel> search(DeviceDataAcquisitionModel probe, Pageable pageable) {
        Assert.isInstanceOf(DeviceDataAcquisition.class, probe);
        Example<DeviceDataAcquisition> example = Example.of((DeviceDataAcquisition) probe);
        return findAll(example, pageable)
                .map(DeviceDataAcquisitionModel.class::cast);
    }

    @Override
    default DeviceDataAcquisitionModel newProbe() {
        return DeviceDataAcquisition.newProbe();
    }

    Optional<DeviceDataAcquisition> findByDaqId(DeviceDataAcquisitionId daqId);
}
