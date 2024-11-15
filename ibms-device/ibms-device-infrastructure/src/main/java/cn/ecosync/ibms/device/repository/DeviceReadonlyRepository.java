package cn.ecosync.ibms.device.repository;

import cn.ecosync.ibms.device.model.DeviceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface DeviceReadonlyRepository {
    Optional<DeviceDto> findByDeviceCode(String deviceCode);

    List<DeviceDto> findAll();

    Page<DeviceDto> findAll(Pageable pageable);
}
