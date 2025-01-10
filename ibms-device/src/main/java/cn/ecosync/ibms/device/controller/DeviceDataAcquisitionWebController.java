package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.device.command.AddDataAcquisitionCommand;
import cn.ecosync.ibms.device.command.RemoveDataAcquisitionCommand;
import cn.ecosync.ibms.device.command.UpdateDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "设备数据采集")
@RestController
@RequiredArgsConstructor
@RequestMapping("/device-daq")
public class DeviceDataAcquisitionWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Operation(summary = "新增数据采集")
    @PostMapping("/add")
    public void execute(@RequestBody @Validated AddDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "修改数据采集")
    @PostMapping("/update")
    public void execute(@RequestBody @Validated UpdateDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "删除数据采集")
    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "获取数据采集")
    @PostMapping("/get")
    public IDeviceDataAcquisition get(@RequestBody @Validated GetDataAcquisitionQuery query) {
        return queryBus.execute(query);
    }

    @Operation(summary = "查询数据采集")
    @PostMapping("/search")
    public PagedModel<DeviceDataAcquisition> search(@RequestBody @Validated SearchDataAcquisitionQuery query) {
        return new PagedModel<>(queryBus.execute(query));
    }
}
