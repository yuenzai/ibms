package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.bacnet.command.AddBacnetDeviceCommand;
import cn.ecosync.ibms.bacnet.command.SaveBacnetDeviceCommand;
import cn.ecosync.ibms.device.command.RemoveDeviceCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.query.SearchDeviceQuery;
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
@RequestMapping("/device")
public class DeviceWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/add-bacnet-device")
    public void execute(@RequestBody @Validated AddBacnetDeviceCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/update-bacnet-device")
    public void execute(@RequestBody @Validated SaveBacnetDeviceCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDeviceCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/get")
    public Device get(@RequestBody @Validated GetDeviceQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/search")
    public Iterable<Device> search(@RequestBody @Validated SearchDeviceQuery query) {
        Pageable pageable = query.toPageable();
        List<Device> devices = queryBus.execute(query);
        return pageable.isPaged() ? new PageImpl<>(devices, pageable, devices.size()) : devices;
    }
}
