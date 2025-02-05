package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.dto.DeviceProbe;
import cn.ecosync.ibms.device.model.DeviceId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DeviceJpaRepository extends JpaRepository<DeviceEntity, Integer> {
    default Example<DeviceEntity> newExample(DeviceProbe probe) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("schemasId.schemasCode", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("deviceName", ExampleMatcher.GenericPropertyMatcher::contains);
        return Example.of(DeviceEntity.newProbe(probe), matcher);
    }

    Optional<DeviceEntity> findByDeviceId(DeviceId deviceId);

    List<DeviceEntity> findByDeviceIdIn(Set<DeviceId> deviceIds);

//    Page<DeviceEntity> findByDataAcquisitionEntities(Set<DeviceDataAcquisitionEntity> dataAcquisitionEntities, Pageable pageable);
}
