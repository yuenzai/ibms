package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.dto.DevicePointDto;
import cn.ecosync.ibms.device.dto.DeviceStatusDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.query.GetDeviceStatusQuery;
import cn.ecosync.ibms.device.query.SearchDevicePointQuery;
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
    public DeviceDto getBacnetDevice(@PathVariable String deviceCode) {
        return queryBus.execute(new GetDeviceQuery(deviceCode)).orElse(null);
    }

    @GetMapping
    public Iterable<DeviceDto> search(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer pageSize
    ) {
        SearchDeviceQuery query = new SearchDeviceQuery(page, pageSize);
        return queryBus.execute(query);
    }

    @GetMapping("/{deviceCode}/point")
    public Iterable<DevicePointDto> searchPoint(@PathVariable String deviceCode) {
        SearchDevicePointQuery query = new SearchDevicePointQuery(deviceCode);
        return queryBus.execute(query);
    }

    @GetMapping("/{deviceCode}/status")
    public DeviceStatusDto getDeviceStatus(@PathVariable String deviceCode) {
        return queryBus.execute(new GetDeviceStatusQuery(deviceCode)).orElse(null);
    }
}
