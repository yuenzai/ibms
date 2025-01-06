package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DeviceSchemasJpaRepository extends JpaRepository<DeviceSchemasEntity, Integer> {
    Optional<DeviceSchemasEntity> findBySchemasId(DeviceSchemasId schemasId);

    List<DeviceSchemasEntity> findBySchemasIdIn(Set<DeviceSchemasId> schemasIds);

    void removeBySchemasId(DeviceSchemasId schemasId);
}
