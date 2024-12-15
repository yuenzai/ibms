package cn.ecosync.ibms.device.model;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface DeviceRepository<T extends DeviceModel> {
    void add(T device);

    void remove(T device);

    Optional<DeviceCommandModel> get(DeviceId deviceId);

    List<T> search(Example<T> example, Sort sort);

    Page<T> search(Example<T> example, Pageable pageable);

    Example<T> newExample(DeviceId deviceIdProbe, DeviceProperties devicePropertiesProbe);
}
