package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.bacnet.command.AddBacnetDeviceCommand;
import cn.ecosync.ibms.bacnet.command.SaveBacnetDeviceCommand;
import cn.ecosync.ibms.device.command.RemoveDeviceCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.query.SearchDeviceQuery;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.query.QueryBus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "设备")
@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
public class DeviceWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Operation(summary = "新增BACnet设备")
    @PostMapping("/add-bacnet-device")
    public void execute(@RequestBody @Validated AddBacnetDeviceCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "更新BACnet设备")
    @PostMapping("/update-bacnet-device")
    public void execute(@RequestBody @Validated SaveBacnetDeviceCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "删除设备")
    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDeviceCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "获取设备")
    @PostMapping("/get")
    public Device get(@RequestBody @Validated GetDeviceQuery query) {
        return queryBus.execute(query);
    }

    @Operation(summary = "查询设备")
    @PostMapping("/search")
    public PagedModel<Device> search(@RequestBody @Validated SearchDeviceQuery query) {
        return new PagedModel<>(queryBus.execute(query));
    }
}
