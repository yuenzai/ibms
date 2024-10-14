package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.model.DeviceDto;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceReadonlyRepository extends PagingAndSortingRepository<DeviceDto, Integer> {
    Optional<DeviceDto> findByDeviceCode(String deviceCode);

    @Override
    List<DeviceDto> findAll();
}
