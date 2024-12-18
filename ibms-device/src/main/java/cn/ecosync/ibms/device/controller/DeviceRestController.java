package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.device.command.CreateDeviceCommand;
import cn.ecosync.ibms.device.command.RemoveDeviceCommand;
import cn.ecosync.ibms.device.command.UpdateDeviceCommand;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.query.ListSearchDeviceQuery;
import cn.ecosync.ibms.device.query.PageSearchDeviceQuery;
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
@RequestMapping("/device")
public class DeviceRestController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/add")
    public void execute(@RequestBody @Validated CreateDeviceCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/update")
    public void execute(@RequestBody @Validated UpdateDeviceCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDeviceCommand command) {
        commandBus.execute(command);
    }

//    @PostMapping("/point/put")
//    public void execute(@RequestBody @Validated PutDevicePointCommand command) {
//        commandBus.execute(command);
//    }
//
//    @PostMapping("/point/remove")
//    public void execute(@RequestBody @Validated RemoveDevicePointCommand command) {
//        commandBus.execute(command);
//    }

    @PostMapping("/get")
    public DeviceModel get(@RequestBody @Validated GetDeviceQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/list-search")
    public List<DeviceModel> search(@RequestBody @Validated ListSearchDeviceQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/page-search")
    public Page<DeviceModel> search(@RequestBody @Validated PageSearchDeviceQuery query) {
        return queryBus.execute(query);
    }
}
