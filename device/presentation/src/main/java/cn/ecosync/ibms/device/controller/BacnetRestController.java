package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.Constants;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.device.command.PutDeviceCommand;
import cn.ecosync.ibms.device.command.PutDevicePointCommand;
import cn.ecosync.ibms.device.command.RemoveDevicePointCommand;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.bacnet.BacnetNetworkProperties;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.query.GetDictionaryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bacnet")
public class BacnetRestController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @GetMapping("/network")
    public BacnetNetworkProperties getBacnetNetwork() {
        GetDictionaryQuery query = new GetDictionaryQuery(new DictionaryKey(BacnetNetworkProperties.TYPE));
        return (BacnetNetworkProperties) queryBus.execute(query).orElse(null);
    }

    @PostMapping("/device")
    public void addBacnetDevice(@RequestBody @Validated(Constants.Create.class) PutDeviceCommand command) {
        command.setNetworkId(BacnetNetworkProperties.TYPE);
        commandBus.execute(command);
    }

    @PutMapping("/device")
    public void updateBacnetDevice(@RequestBody @Validated(Constants.Update.class) PutDeviceCommand command) {
        command.setNetworkId(BacnetNetworkProperties.TYPE);
        commandBus.execute(command);
    }

    @GetMapping("/device/{deviceCode}")
    public DeviceDto getBacnetDevice(@PathVariable String deviceCode) {
        GetDeviceQuery query = new GetDeviceQuery(deviceCode);
        return queryBus.execute(query).orElse(null);
    }

    @PostMapping("/device/point")
    public void putBacnetDevicePoint(@RequestBody @Validated PutDevicePointCommand command) {
        commandBus.execute(command);
    }

    @PutMapping("/device/point")
    public void removeBacnetDevicePoint(@RequestBody @Validated RemoveDevicePointCommand command) {
        commandBus.execute(command);
    }
}
