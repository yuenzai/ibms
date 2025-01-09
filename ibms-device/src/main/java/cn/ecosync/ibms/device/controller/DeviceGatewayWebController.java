package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.device.command.AddGatewayCommand;
import cn.ecosync.ibms.device.command.RemoveGatewayCommand;
import cn.ecosync.ibms.device.command.SaveGatewayCommand;
import cn.ecosync.ibms.device.command.SetGatewaySynchronizationStateCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.query.GetGatewayQuery;
import cn.ecosync.ibms.device.query.SearchGatewayQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum.SYNCHRONIZED;
import static cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum.SYNCHRONIZING;

@Tag(name = "设备网关")
@RestController
@RequiredArgsConstructor
@RequestMapping("/device-gateway")
public class DeviceGatewayWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Operation(hidden = true)
    @PostMapping("/add")
    public void execute(@RequestBody @Validated AddGatewayCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "更新网关")
    @PostMapping("/save")
    public void execute(@RequestBody @Validated SaveGatewayCommand command) {
        commandBus.execute(command);
    }

    @Operation(hidden = true)
    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveGatewayCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "同步网关")
    @PostMapping("/sync")
    public void execute(@RequestBody @Validated SetGatewaySynchronizationStateCommand command) {
        command.setSynchronizationState(SYNCHRONIZING);
        commandBus.execute(command);
    }

    @Operation(summary = "获取网关")
    @PostMapping("/get")
    public DeviceGateway execute(@RequestBody @Validated GetGatewayQuery query) {
        return doExecute(query, false);
    }

    @Operation(summary = "查询网关")
    @PostMapping("/search")
    public Page<DeviceGateway> execute(@RequestBody @Validated SearchGatewayQuery query) {
        return queryBus.execute(query);
    }

    @Operation(hidden = true)
    @GetMapping("/{gateway-code}")
    public DeferredResult<ResponseEntity<DeviceGateway>> get(@PathVariable("gateway-code") String gatewayCode, @RequestParam(name = "initial", defaultValue = "false") Boolean initial) {
        DeferredResult<ResponseEntity<DeviceGateway>> result = new DeferredResult<>(null, () -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
        GetGatewayQuery query = new GetGatewayQuery(gatewayCode);
        DeviceGateway gateway;
        if (initial) {
            gateway = doExecute(query, false);
        } else {
            gateway = doExecute(query, true);
        }
        if (gateway != null) result.setResult(new ResponseEntity<>(gateway, HttpStatus.OK));
        return result;
    }

    @Operation(hidden = true)
    @GetMapping("/{gateway-code}/synchronized")
    public void onEvent(@PathVariable("gateway-code") String gatewayCode) {
        SetGatewaySynchronizationStateCommand command = new SetGatewaySynchronizationStateCommand(gatewayCode, SYNCHRONIZED);
        commandBus.execute(command);
    }

    private DeviceGateway doExecute(GetGatewayQuery query, boolean isWebhook) {
        DeviceGateway gateway = queryBus.execute(query);
        if (gateway == null) return null;
        if (isWebhook && gateway.getSynchronizationState() != SYNCHRONIZING) return null;
        List<DeviceDataAcquisition> dataAcquisitions = gateway.getDataAcquisitions().stream()
                .map(DeviceDataAcquisition::getDataAcquisitionId)
                .map(GetDataAcquisitionQuery::new)
                .map(queryBus::execute)
                .map(DeviceDataAcquisition.class::cast)
                .collect(Collectors.toList());
        return gateway.withDataAcquisitions(dataAcquisitions);
    }
}
