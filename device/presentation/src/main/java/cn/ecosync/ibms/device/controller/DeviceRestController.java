package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.query.SearchDeviceQuery;
import cn.ecosync.ibms.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
public class DeviceRestController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping
    public void execute(@RequestBody @Validated Command command) {
        commandBus.execute(command);
    }

    @GetMapping("/{deviceCode}")
    public DeviceDto get(@PathVariable String deviceCode, @RequestParam(value = "readonly", defaultValue = "false") Boolean readonly) {
        return queryBus.execute(new GetDeviceQuery(deviceCode, readonly)).orElse(null);
    }

    @GetMapping
    public Iterable<DeviceDto> search(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer pageSize,
            @RequestParam(value = "readonly", defaultValue = "false") Boolean readonly
    ) {
        return queryBus.execute(new SearchDeviceQuery(page, pageSize, readonly));
    }
}
