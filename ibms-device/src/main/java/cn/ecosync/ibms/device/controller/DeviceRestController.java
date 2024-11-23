package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.command.*;
import cn.ecosync.ibms.dto.DeviceDto;
import cn.ecosync.ibms.query.GetDeviceQuery;
import cn.ecosync.ibms.query.SearchDeviceListQuery;
import cn.ecosync.ibms.query.SearchDevicePageQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
public class DeviceRestController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/add")
    public void execute(@RequestBody @Validated AddDeviceCommand command) {
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

    @PostMapping("/point/put")
    public void execute(@RequestBody @Validated PutDevicePointCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/point/remove")
    public void execute(@RequestBody @Validated RemoveDevicePointCommand command) {
        commandBus.execute(command);
    }

    @GetMapping("/{deviceCode}")
    public DeviceDto get(@PathVariable String deviceCode, @RequestParam(value = "readonly", defaultValue = "false") Boolean readonly) {
        return queryBus.execute(new GetDeviceQuery(deviceCode, readonly));
    }

    @GetMapping
    public Iterable<DeviceDto> search(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer pageSize,
            @RequestParam(value = "readonly", defaultValue = "false") Boolean readonly
    ) {
        Pageable pageable = CollectionUtils.of(page, pageSize);
        if (pageable.isPaged()) {
            return queryBus.execute(new SearchDevicePageQuery(page, pageSize, readonly));
        } else {
            return queryBus.execute(new SearchDeviceListQuery(readonly));
        }
    }
}
