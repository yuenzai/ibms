package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.query.SearchDeviceListQuery;
import cn.ecosync.ibms.device.query.SearchDevicePageQuery;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.util.CollectionUtils;
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

    @PostMapping
    public void execute(@RequestBody @Validated Command command) {
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
