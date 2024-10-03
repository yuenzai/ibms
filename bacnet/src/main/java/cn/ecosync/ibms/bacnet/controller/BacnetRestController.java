package cn.ecosync.ibms.bacnet.controller;

import cn.ecosync.ibms.bacnet.BacnetApplicationService;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery.BacnetObjectProperties;
import cn.ecosync.ibms.device.model.bacnet.*;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
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
@RequestMapping("/bacnet")
public class BacnetRestController {
    private final BacnetApplicationService bacnetApplicationService;

    @PostMapping("/readpropm")
    public List<ReadPropertyMultipleAck> readpropm(@RequestBody @Validated BacnetReadPropertyMultipleQuery query) {
        try {
            return bacnetApplicationService.readpropm(query);
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }

    @GetMapping("/device/{deviceInstance}/object-ids")
    public List<BacnetObject> getDeviceObjectIds(@PathVariable Integer deviceInstance) {
        BacnetObject deviceObject = new BacnetObject(BacnetObjectType.OBJECT_DEVICE, deviceInstance);
        BacnetProperty objectIdsProperty = new BacnetProperty(BacnetPropertyId.PROP_OBJECT_LIST, null);
        BacnetObjectProperties objectProperties = new BacnetObjectProperties(deviceObject, Collections.singletonList(objectIdsProperty));
        BacnetReadPropertyMultipleQuery query = new BacnetReadPropertyMultipleQuery(deviceInstance, Collections.singletonList(objectProperties));
        try {
            ReadPropertyMultipleAck ack = CollectionUtils.firstElement(bacnetApplicationService.readpropm(query));
            if (ack == null) {
                return Collections.emptyList();
            }
            ReadPropertyMultipleAck.Property objectIdsPropertyValue = CollectionUtils.firstElement(ack.getProperties());
            if (objectIdsPropertyValue == null) {
                return Collections.emptyList();
            }
            BacnetError bacnetError = objectIdsPropertyValue.getError().orElse(null);
            if (bacnetError != null) {
                log.info("find bacnet error: {}", bacnetError);
                return Collections.emptyList();
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
