package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionCommandModel;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.repository.DeviceDataAcquisitionRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

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
        return findAll(newExample(probe), sort).stream()
                .map(DeviceDataAcquisitionModel.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    default Page<DeviceDataAcquisitionModel> search(DeviceDataAcquisitionModel probe, Pageable pageable) {
        return findAll(newExample(probe), pageable)
                .map(DeviceDataAcquisitionModel.class::cast);
    }

    default Example<DeviceDataAcquisition> newExample(DeviceDataAcquisitionModel model) {
        DeviceDataAcquisition probe = DeviceDataAcquisition.newProbe(model);
        return Example.of(probe);
    }

    Optional<DeviceDataAcquisition> findByDaqId(DeviceDataAcquisitionId daqId);
}
