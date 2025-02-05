package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.device.GatewaySynchronizationService;
import cn.ecosync.ibms.device.command.AddGatewayCommand;
import cn.ecosync.ibms.device.command.GatewayCommand;
import cn.ecosync.ibms.device.command.RemoveGatewayCommand;
import cn.ecosync.ibms.device.command.SetGatewaySynchronizationStateCommand;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.query.GetGatewayQuery;
import cn.ecosync.ibms.device.query.SearchGatewayQuery;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.serde.JsonSerde;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import static cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum.SYNCHRONIZING;

@Tag(name = "设备网关")
@RestController
@RequiredArgsConstructor
@RequestMapping("/device-gateway")
public class DeviceGatewayWebController {
    private final GatewaySynchronizationService gatewaySynchronizationService;
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final JsonSerde jsonSerde;

    @Operation(summary = "执行网关命令")
    @PostMapping(path = "/{gateway-code}", headers = "Command-Type")
    public ResponseEntity<Void> execute(@PathVariable("gateway-code") String gatewayCode, RequestEntity<String> request) {
        DeviceGatewayId gatewayId = new DeviceGatewayId(gatewayCode);
        String commandType = request.getHeaders().getFirst("Command-Type");
        if (commandType == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        GatewayCommand command;
        switch (commandType) {
            case "ADD":
                commandBus.execute(new AddGatewayCommand(gatewayId));
                return new ResponseEntity<>(HttpStatus.OK);
            case "REMOVE":
                commandBus.execute(new RemoveGatewayCommand(gatewayId));
                return new ResponseEntity<>(HttpStatus.OK);
            case "SET_SYNCHRONIZATION_STATE":
                command = jsonSerde.deserialize(request.getBody(), SetGatewaySynchronizationStateCommand.class)
                        .withGatewayId(gatewayId);
                commandBus.execute(command);
                return new ResponseEntity<>(HttpStatus.OK);
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "查询网关")
    @GetMapping(headers = "Query-Type=search")
    public PagedModel<DeviceGateway> execute(@RequestParam(name = "page", required = false) Integer page,
                                             @RequestParam(name = "pagesize", required = false) Integer pagesize) {
        SearchGatewayQuery query = new SearchGatewayQuery(page, pagesize);
        return new PagedModel<>(queryBus.execute(query));
    }

    @Operation(hidden = true)
    @GetMapping(path = "/{gateway-code}", headers = "Query-Type=get")
    public DeviceGateway get(@PathVariable("gateway-code") String gatewayCode) {
        DeviceGatewayId gatewayId = new DeviceGatewayId(gatewayCode);
        GetGatewayQuery query = new GetGatewayQuery(gatewayId);
        return gatewaySynchronizationService.handle(query);
    }

    @Operation(hidden = true)
    @GetMapping(path = "/{gateway-code}", headers = "Query-Type=poll")
    public DeferredResult<ResponseEntity<DeviceGateway>> poll(@PathVariable("gateway-code") String gatewayCode) {
        DeferredResult<ResponseEntity<DeviceGateway>> result = new DeferredResult<>(null, () -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
        DeviceGatewayId gatewayId = new DeviceGatewayId(gatewayCode);
        GetGatewayQuery query = new GetGatewayQuery(gatewayId, SYNCHRONIZING);
        gatewaySynchronizationService.poll(gatewayId, query, in -> {
            if (in != null) {
                result.setResult(new ResponseEntity<>(in, HttpStatus.OK));
            } else {
                result.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
            }
        });
        return result;
    }
}
