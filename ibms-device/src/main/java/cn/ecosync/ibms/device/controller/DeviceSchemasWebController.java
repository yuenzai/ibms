package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.bacnet.command.SaveBacnetSchemasCommand;
import cn.ecosync.ibms.device.command.RemoveDeviceSchemasCommand;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.query.GetSchemasQuery;
import cn.ecosync.ibms.device.query.SearchSchemasQuery;
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

@Tag(name = "设备模型")
@RestController
@RequiredArgsConstructor
@RequestMapping("/device-schemas")
public class DeviceSchemasWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Operation(summary = "保存设备模型")
    @PostMapping("/save/bacnet")
    public void execute(@RequestBody @Validated SaveBacnetSchemasCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "删除设备模型")
    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDeviceSchemasCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "获取设备模型")
    @PostMapping("/get")
    public DeviceSchemas get(@RequestBody @Validated GetSchemasQuery query) {
        return queryBus.execute(query);
    }

    @Operation(summary = "查询设备模型")
    @PostMapping("/search")
    public PagedModel<DeviceSchemas> search(@RequestBody @Validated SearchSchemasQuery query) {
        return new PagedModel<>(queryBus.execute(query));
    }
}
