package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.bacnet.command.SaveBacnetSchemasCommand;
import cn.ecosync.ibms.device.command.RemoveDeviceSchemasCommand;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.query.GetSchemasQuery;
import cn.ecosync.ibms.device.query.SearchSchemasQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device-schemas")
public class DeviceSchemasWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/save/bacnet")
    public void execute(@RequestBody @Validated SaveBacnetSchemasCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDeviceSchemasCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/get")
    public DeviceSchemas get(@RequestBody @Validated GetSchemasQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/search")
    public Iterable<DeviceSchemas> search(@RequestBody @Validated SearchSchemasQuery query) {
        Pageable pageable = query.toPageable();
        List<DeviceSchemas> deviceschemasList = queryBus.execute(query);
        return pageable.isPaged() ? new PageImpl<>(deviceschemasList, pageable, deviceschemasList.size()) : deviceschemasList;
    }
}
