package cn.ecosync.ibms.controller;

import cn.ecosync.ibms.bacnet.command.BacnetWritePropertyCommand;
import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleBatchQuery;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.bacnet.query.BacnetWhoIsQuery;
import cn.ecosync.ibms.bacnet.query.ListSearchBacnetDeviceObjectIdsQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.util.CollectionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bacnet")
@Tag(name = "bacnet", description = "BACnet API")
public class BacnetRestController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/service/who-is")
    public List<BacnetDeviceAddress> execute(@RequestBody @Validated BacnetWhoIsQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/service/read-property-multiple")
    @Operation(summary = "Execute ReadPropertyMultiple service", description = "Execute ReadPropertyMultiple service", tags = {"bacnet"})
    public ReadPropertyMultipleAck execute(@RequestBody @Validated BacnetReadPropertyMultipleQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/service/read-property-multiple/batch")
    public List<ReadPropertyMultipleAck> execute(@RequestBody @Validated BacnetReadPropertyMultipleBatchQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/service/write-property")
    public void execute(@RequestBody @Validated BacnetWritePropertyCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/device-object-ids/list-search")
    public List<BacnetObject> getDeviceObjectIds(@RequestBody @Validated ListSearchBacnetDeviceObjectIdsQuery query) {
        Integer deviceInstance = query.getDeviceInstance();
        BacnetObject deviceObject = new BacnetObject(BacnetObjectType.OBJECT_DEVICE, deviceInstance);
        BacnetProperty objectIdsProperty = new BacnetProperty(BacnetPropertyId.PROP_OBJECT_LIST, null);
        BacnetObjectProperties objectProperties = new BacnetObjectProperties(deviceObject, Collections.singleton(objectIdsProperty));
        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, Collections.singleton(objectProperties));
        BacnetReadPropertyMultipleQuery readpropmQuery = new BacnetReadPropertyMultipleQuery(service);
        try {
            ReadPropertyMultipleAck.Property objectIdsPropertyValue = Optional.ofNullable(queryBus.execute(readpropmQuery))
                    .map(in -> CollectionUtils.firstElement(in.getValues()))
                    .map(in -> CollectionUtils.firstElement(in.getProperties()))
                    .orElse(null);
            if (objectIdsPropertyValue == null) {
                return Collections.emptyList();
            }
            BacnetError bacnetError = objectIdsPropertyValue.getError().orElse(null);
            if (bacnetError != null) {
                throw new RuntimeException(bacnetError.toString());
            }
            return objectIdsPropertyValue.getPropertyValues().stream()
                    .filter(in -> in instanceof BacnetPropertyValue.OBJECT_ID)
                    .map(in -> ((BacnetPropertyValue.OBJECT_ID) in).getValue())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }
}
