package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.model.DeviceGateway;

import java.util.Optional;

public interface GatewayRepository {
    void save(DeviceGateway gateway);

    Optional<DeviceGateway> get();
}
