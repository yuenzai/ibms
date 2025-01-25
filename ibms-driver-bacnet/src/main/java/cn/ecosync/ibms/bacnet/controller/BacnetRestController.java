package cn.ecosync.ibms.bacnet.controller;

import cn.ecosync.ibms.JsonSerdeContextHolder;
import cn.ecosync.ibms.bacnet.command.BacnetWritePropertyCommand;
import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.model.BacnetInstrumentation;
import cn.ecosync.ibms.bacnet.query.BacnetWhoIsQuery;
import cn.ecosync.ibms.bacnet.query.ListSearchBacnetDeviceObjectIdsQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.serde.JsonSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.bacnet.dto.BacnetPropertyId.PROP_OBJECT_LIST;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bacnet")
public class BacnetRestController {
    private final JsonSerde jsonSerde;
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/service/who-is")
    public List<BacnetDeviceAddress> execute(@RequestBody @Validated BacnetWhoIsQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/service/write-property")
    public void execute(@RequestBody @Validated BacnetWritePropertyCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/device-object-ids/list-search")
    public List<BacnetObject> getDeviceObjectIds(@RequestBody @Validated ListSearchBacnetDeviceObjectIdsQuery query) {
        Integer deviceInstance = query.getDeviceInstance();
        BacnetObject deviceObject = new BacnetObject(BacnetObjectType.DEVICE, deviceInstance);
        BacnetProperty objectIdsProperty = PROP_OBJECT_LIST.toBacnetProperty();
        BacnetObjectProperties objectProperties = new BacnetObjectProperties(deviceObject, objectIdsProperty);
        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, objectProperties);
        ReadPropertyMultipleAck ack;
        try {
            JsonSerdeContextHolder.set(jsonSerde);
            ack = BacnetInstrumentation.doScrape(service);
            JsonSerdeContextHolder.clear();
            BacnetPropertyResult objectIdsPropertyResult = Optional.ofNullable(ack.toMap())
                    .map(in -> in.get(deviceObject))
                    .map(in -> in.get(objectIdsProperty))
                    .orElse(null);
            if (objectIdsPropertyResult == null) {
                return Collections.emptyList();
            }
            BacnetError bacnetError = objectIdsPropertyResult.getError();
            if (bacnetError != null) {
                throw new RuntimeException(bacnetError.toString());
            }
            return objectIdsPropertyResult.getValues().stream()
                    .filter(in -> in instanceof BacnetPropertyValue.OBJECT_ID)
                    .map(in -> ((BacnetPropertyValue.OBJECT_ID) in).getValue())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }
}
