package cn.ecosync.ibms.bacnet.controller;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.model.*;
import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.service.BacnetApplicationService;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DevicePointDto;
import cn.ecosync.ibms.device.model.DeviceStatus;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/" + BacnetConstants.BACNET)
public class BacnetRestController {
    private final BacnetApplicationService bacnetApplicationService;
//    private final File DATA_DIR = new File(System.getProperty("java.io.tmpdir") + "/bacnet");

    @PostMapping("/readpropm")
    public DeviceStatus readpropm(@RequestBody @Validated DeviceDto deviceDto) {
        List<ReadPropertyMultipleAck> ack = BacnetReadPropertyMultipleService.newInstance(deviceDto)
                .map(bacnetApplicationService::readPropertyMultiple)
                .orElse(Collections.emptyList());

        Map<BacnetObjectProperty, BacnetPropertyValue> valueMap = ReadPropertyMultipleAck.toMap(ack);

        List<DevicePointDto> devicePoints = deviceDto.getDevicePoints();
        Map<String, Object> deviceStatus = CollectionUtils.newHashMap(devicePoints.size());
        for (DevicePointDto devicePoint : devicePoints) {
            if (!(devicePoint.getPointExtra() instanceof BacnetDevicePointExtra)) {
                continue;
            }
            BacnetObjectProperty bop = ((BacnetDevicePointExtra) devicePoint.getPointExtra()).getBacnetObjectProperty();
            Object pointValue = Optional.ofNullable(valueMap.get(bop))
                    .map(BacnetPropertyValue::toObject)
                    .orElse(null);
            deviceStatus.put(devicePoint.getPointCode(), pointValue);
        }
        return new DeviceStatus(deviceStatus, System.currentTimeMillis());
    }

//    @PostMapping("/readpropm")
//    public List<ReadPropertyMultipleAck> readpropm(@RequestBody @Validated BacnetReadPropertyMultipleService service) {
//        return bacnetApplicationService.readPropertyMultiple(service);
//    }
//
//    @PostMapping("/readpropmToFile")
//    public void readpropmToFile(@RequestBody @Validated BacnetReadPropertyMultipleService service) {
//        try {
//            int exitCode = BacnetApplicationService.readPropertyMultiple(service, DATA_DIR);
//            if (exitCode != 0) {
//                log.error("readpropm exit code: {}", exitCode);
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        }
//    }

    @GetMapping("/device/{deviceInstance}/object-ids")
    public List<BacnetObject> getDeviceObjectIds(@PathVariable Integer deviceInstance) {
        BacnetObject deviceObject = new BacnetObject(BacnetObjectType.OBJECT_DEVICE, deviceInstance);
        BacnetProperty objectIdsProperty = new BacnetProperty(BacnetPropertyId.PROP_OBJECT_LIST, null);
        BacnetObjectProperties objectProperties = new BacnetObjectProperties(deviceObject, Collections.singletonList(objectIdsProperty));
        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, Collections.singletonList(objectProperties));
        try {
            ReadPropertyMultipleAck ack = CollectionUtils.firstElement(bacnetApplicationService.readPropertyMultiple(service));
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

//    @Override
//    public void run(ApplicationArguments args) {
//        if (!DATA_DIR.exists()) {
//            DATA_DIR.mkdirs();
//        }
//    }
}
