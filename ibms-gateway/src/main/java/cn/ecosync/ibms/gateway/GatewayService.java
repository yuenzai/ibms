package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.command.SetGatewaySynchronizationStateCommand;
import cn.ecosync.ibms.device.model.DeviceGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/ibms/device-gateway")
public interface GatewayService {
    @HttpExchange(method = "GET", url = "/{gateway-code}", headers = "Query-Type=get")
    ResponseEntity<DeviceGateway> get(@PathVariable("gateway-code") String gatewayCode);

    @HttpExchange(method = "GET", url = "/{gateway-code}", headers = "Query-Type=poll")
    ResponseEntity<DeviceGateway> poll(@PathVariable("gateway-code") String gatewayCode);

    @HttpExchange(method = "POST", url = "/{gateway-code}", headers = "Command-Type=SET_SYNCHRONIZATION_STATE")
    void execute(@PathVariable("gateway-code") String gatewayCode, @RequestBody SetGatewaySynchronizationStateCommand command);
}
