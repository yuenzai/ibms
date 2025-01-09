package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.model.DeviceGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/ibms/device-gateway")
public interface GatewayService {
    @GetExchange("/{gateway-code}")
    ResponseEntity<DeviceGateway> get(@PathVariable("gateway-code") String gatewayCode, @RequestParam(name = "initial", defaultValue = "false") Boolean initial);

    @GetExchange("/{gateway-code}/synchronized")
    void notifySynchronized(@PathVariable("gateway-code") String gatewayCode);
}
