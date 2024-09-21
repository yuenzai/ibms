package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.Constants;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.device.command.PutDeviceCommand;
import cn.ecosync.ibms.device.command.PutDevicePointCommand;
import cn.ecosync.ibms.device.command.RemoveDevicePointCommand;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.bacnet.BacnetNetworkDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;
import cn.ecosync.ibms.system.command.PutDictionaryCommand;
import cn.ecosync.ibms.system.query.GetDictionaryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bacnet")
public class BacnetRestController {
    public static final String DICTIONARY_KEY = "bacnet-network";
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final JsonSerde jsonSerde;

    @PutMapping("/network")
    public void putBacnetNetwork(@RequestBody BacnetNetworkDto vo) {
        Map<String, Object> value = jsonSerde.convertValue(vo, new TypeReference<Map<String, Object>>() {
                })
                .orElseThrow(IllegalArgumentException::new);
        PutDictionaryCommand command = new PutDictionaryCommand(DICTIONARY_KEY, value);
        commandBus.execute(command);
    }

    @GetMapping("/network")
    public BacnetNetworkDto getBacnetNetwork() {
        GetDictionaryQuery query = new GetDictionaryQuery(DICTIONARY_KEY);
        Map<String, Object> dictValue = queryBus.execute(query);
        return jsonSerde.convertValue(dictValue, BacnetNetworkDto.class)
                .orElseThrow(IllegalArgumentException::new);
    }

    @PostMapping("/device")
    public void addBacnetDevice(@RequestBody @Validated(Constants.Create.class) PutDeviceCommand command) {
        commandBus.execute(command);
    }

    @PutMapping("/device")
    public void updateBacnetDevice(@RequestBody @Validated(Constants.Update.class) PutDeviceCommand command) {
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
