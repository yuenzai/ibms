package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.device.command.AddBacnetDeviceCommand;
import cn.ecosync.ibms.device.command.AddDeviceDataAcquisitionCommand;
import cn.ecosync.ibms.device.command.RemoveDeviceDataAcquisitionCommand;
import cn.ecosync.ibms.device.command.UpdateDeviceDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.query.GetDeviceDataAcquisitionQuery;
import cn.ecosync.ibms.device.query.ListSearchDeviceDataAcquisitionQuery;
import cn.ecosync.ibms.device.query.PageSearchDeviceDataAcquisitionQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device-daq")
public class DeviceDataAcquisitionWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/add")
    public void execute(@RequestBody @Validated AddDeviceDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/add-bacnet-device")
    public void execute(@RequestBody @Validated AddBacnetDeviceCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/update")
    public void execute(@RequestBody @Validated UpdateDeviceDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDeviceDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/get")
    public DeviceDataAcquisitionModel get(@RequestBody @Validated GetDeviceDataAcquisitionQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/list-search")
    public List<DeviceDataAcquisitionModel> search(@RequestBody @Validated ListSearchDeviceDataAcquisitionQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/page-search")
    public Page<DeviceDataAcquisitionModel> search(@RequestBody @Validated PageSearchDeviceDataAcquisitionQuery query) {
        return queryBus.execute(query);
    }
}
