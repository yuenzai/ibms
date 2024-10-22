package cn.ecosync.ibms.bacnet.controller;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.exception.BacnetErrorException;
import cn.ecosync.ibms.bacnet.model.*;
import cn.ecosync.ibms.bacnet.service.BacnetApplicationService;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.service.BacnetWhoIs;
import cn.ecosync.ibms.bacnet.service.BacnetWriteProperty;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/" + BacnetConstants.BACNET)
public class BacnetRestController {
    private final BacnetApplicationService bacnetApplicationService;

    @PostMapping("/service/who-is")
    public List<BacnetDeviceAddress> execute(@RequestBody @Validated BacnetWhoIs service) throws Exception {
        return bacnetApplicationService.execute(service);
    }

    @PostMapping("/service/read-property-multiple")
    public ReadPropertyMultipleAck execute(@RequestBody @Validated BacnetReadPropertyMultiple service) throws Exception {
        return bacnetApplicationService.execute(service).orElse(null);
    }

    @PostMapping("/service/read-property-multiple/batch")
    public List<ReadPropertyMultipleAck> execute(@RequestBody @Validated List<BacnetReadPropertyMultiple> services) throws Exception {
        return bacnetApplicationService.execute(services);
    }

    @PostMapping("/service/write-property")
    public void execute(@RequestBody @Validated BacnetWriteProperty service) throws Exception {
        bacnetApplicationService.execute(service);
    }

    @GetMapping("/device/{deviceInstance}/object-ids")
    public List<BacnetObject> getDeviceObjectIds(@PathVariable Integer deviceInstance) {
        BacnetObject deviceObject = new BacnetObject(BacnetObjectType.OBJECT_DEVICE, deviceInstance);
        BacnetProperty objectIdsProperty = new BacnetProperty(BacnetPropertyId.PROP_OBJECT_LIST, null);
        BacnetObjectProperties objectProperties = new BacnetObjectProperties(deviceObject, Collections.singletonList(objectIdsProperty));
        BacnetReadPropertyMultiple service = new BacnetReadPropertyMultiple(deviceInstance, Collections.singletonList(objectProperties));
        try {
            ReadPropertyMultipleAck.Property objectIdsPropertyValue = bacnetApplicationService.execute(service)
                    .map(in -> CollectionUtils.firstElement(in.getValues()))
                    .map(in -> CollectionUtils.firstElement(in.getProperties()))
                    .orElse(null);
            if (objectIdsPropertyValue == null) {
                return Collections.emptyList();
            }
            BacnetError bacnetError = objectIdsPropertyValue.getError().orElse(null);
            if (bacnetError != null) {
                throw new BacnetErrorException(bacnetError);
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
