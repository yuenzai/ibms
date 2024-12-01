package cn.ecosync.ibms.device.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface DeviceQueryModelRepository {
    Optional<DeviceQueryModel> findByDeviceCode(String deviceCode);

    List<DeviceQueryModel> search();

    Page<DeviceQueryModel> search(Pageable pageable);

    void save(DeviceQueryModel device);

    void remove(String deviceCode);
}
