package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.device.command.RemoveGatewayCommand;
import cn.ecosync.ibms.device.command.SaveGatewayCommand;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.query.GetGatewayQuery;
import cn.ecosync.ibms.device.query.SearchGatewayQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.util.DeferredResultMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway")
public class DeviceGatewayWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final DeferredResultMap<DeviceGatewayId, DeviceGateway> deviceGatewayPromise;

    @PostMapping("/save")
    public void execute(@RequestBody @Validated SaveGatewayCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveGatewayCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/search")
    public Iterable<DeviceGateway> execute(@RequestBody @Validated SearchGatewayQuery query) {
        Pageable pageable = query.toPageable();
        List<DeviceGateway> gateways = queryBus.execute(query);
        return pageable.isPaged() ? new PageImpl<>(gateways, pageable, gateways.size()) : gateways;
    }

    @GetMapping("/{gateway-code}")
    public DeviceGateway get(@PathVariable("gateway-code") String gatewayCode) {
        GetGatewayQuery query = new GetGatewayQuery(gatewayCode);
        return queryBus.execute(query);
    }

    @GetMapping("/{gateway-code}/webhook")
    public DeferredResult<DeviceGateway> webhook(@PathVariable("gateway-code") String gatewayCode) {
        DeviceGatewayId gatewayId = new DeviceGatewayId(gatewayCode);
        DeferredResult<DeviceGateway> result = new DeferredResult<>();
        result.onCompletion(() -> deviceGatewayPromise.remove(gatewayId));
        deviceGatewayPromise.put(gatewayId, result);
        return result;
    }
}
