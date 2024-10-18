package cn.ecosync.ibms.bacnet.controller;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.exception.BacnetErrorException;
import cn.ecosync.ibms.bacnet.model.*;
import cn.ecosync.ibms.bacnet.service.BacnetApplicationService;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.service.BacnetWhoIs;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DevicePointDto;
import cn.ecosync.ibms.device.model.DeviceStatus;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/" + BacnetConstants.BACNET)
public class BacnetRestController {
    private final BacnetApplicationService bacnetApplicationService;

    @PostMapping("/readpropm")
    public DeviceStatus readpropm(@RequestBody DeviceDto deviceDto) throws Exception {
        BacnetReadPropertyMultiple service = BacnetReadPropertyMultiple.newInstance(deviceDto);
        ReadPropertyMultipleAck ack = bacnetApplicationService.execute(service).orElse(null);
        if (ack == null) {
            return null;
        }
        return toDeviceStatus(deviceDto, ack);
    }

    @PostMapping("/readpropm/batch")
    public List<DeviceStatus> readpropmBatch(@RequestBody List<DeviceDto> deviceDtoList) throws Exception {
        List<BacnetReadPropertyMultiple> services = deviceDtoList.stream()
                .map(BacnetReadPropertyMultiple::newInstance)
                .collect(Collectors.toList());

        List<ReadPropertyMultipleAck> acks = bacnetApplicationService.execute(services);

        Map<Integer, DeviceDto> deviceInstanceMap = CollectionUtils.newHashMap(deviceDtoList.size());
        for (DeviceDto deviceDto : deviceDtoList) {
            Integer deviceInstance = deviceInstanceOf(deviceDto).orElse(null);
            if (deviceInstance == null) {
                continue;
            }
            deviceInstanceMap.put(deviceInstance, deviceDto);
        }

        List<DeviceStatus> deviceStatusList = new ArrayList<>(deviceDtoList.size());
        for (ReadPropertyMultipleAck ack : acks) {
            DeviceDto deviceDto = deviceInstanceMap.get(ack.getDeviceInstance());
            if (deviceDto == null) {
                continue;
            }
            DeviceStatus deviceStatus = toDeviceStatus(deviceDto, ack);
            deviceStatusList.add(deviceStatus);
        }
        return deviceStatusList;
    }

    private DeviceStatus toDeviceStatus(DeviceDto deviceDto, ReadPropertyMultipleAck ack) {
        Map<BacnetObjectProperty, BacnetPropertyValue> valueMap = ack.flatMap();

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
        return new DeviceStatus(deviceDto.getDeviceCode(), deviceStatus, System.currentTimeMillis());
    }

    private Optional<Integer> deviceInstanceOf(DeviceDto deviceDto) {
        return Optional.ofNullable(deviceDto)
                .filter(in -> in.getDeviceExtra() instanceof BacnetDeviceExtra)
                .map(in -> (BacnetDeviceExtra) in.getDeviceExtra())
                .map(BacnetDeviceExtra::getDeviceInstance);
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

    @GetMapping("/service/who-is")
    public List<BacnetDeviceAddress> whoIs() throws Exception {
        BacnetWhoIs whoIs = new BacnetWhoIs();
        return bacnetApplicationService.execute(whoIs);
    }
}
