package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.device.command.RemoveGatewayCommand;
import cn.ecosync.ibms.device.command.SaveGatewayCommand;
import cn.ecosync.ibms.device.command.SyncGatewayCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.query.GetGatewayQuery;
import cn.ecosync.ibms.device.query.SearchGatewayQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum.SYNCHRONIZING;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway")
public class DeviceGatewayWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/save")
    public void execute(@RequestBody @Validated SaveGatewayCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveGatewayCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/sync")
    public void execute(@RequestBody @Validated SyncGatewayCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/get")
    public DeviceGateway execute(@RequestBody @Validated GetGatewayQuery query) {
        return doExecute(query, false);
    }

    @PostMapping("/search")
    public Page<DeviceGateway> execute(@RequestBody @Validated SearchGatewayQuery query) {
        return queryBus.execute(query);
    }

    @GetMapping("/{gateway-code}")
    public DeferredResult<DeviceGateway> get(@PathVariable("gateway-code") String gatewayCode) {
        DeferredResult<DeviceGateway> result = new DeferredResult<>();
        GetGatewayQuery query = new GetGatewayQuery(gatewayCode);
        DeviceGateway gateway = doExecute(query, true);
        if (gateway != null) {
            result.setResult(gateway);
            //todo SYNCHRONIZING -> SYNCHRONIZED
        }
        //todo return http status code 204 when result is null
        return result;
    }

//    @GetMapping("/")
//    public void setSynchronized() {
//
//    }

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
