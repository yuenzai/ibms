package cn.ecosync.ibms.device;

import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.iframework.util.DeferredResultMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceConfiguration {
    @Bean
    public DeferredResultMap<DeviceGatewayId, DeviceGateway> deviceGatewayPromise() {
        return new DeferredResultMap<>();
    }
}
