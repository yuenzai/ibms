package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.model.DeviceGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/gateway")
public interface GatewayService {
    @GetExchange("/{gateway-code}")
    ResponseEntity<DeviceGateway> get(@PathVariable("gateway-code") String gatewayCode);

    @GetExchange("/{gateway-code}/synchronized")
    void notifySynchronized(@PathVariable("gateway-code") String gatewayCode);
}
