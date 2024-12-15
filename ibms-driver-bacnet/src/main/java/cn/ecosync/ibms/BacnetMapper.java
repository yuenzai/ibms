package cn.ecosync.ibms;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectPropertyWithKey;
import cn.ecosync.ibms.bacnet.dto.BacnetProperty;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionProperties;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BacnetMapper {
    public static BacnetReadPropertyMultipleService toReadPropertyMultipleService(
            Integer deviceInstance, DeviceDataAcquisitionProperties.BACnet daqProperties) {
        List<BacnetObjectPropertyWithKey> bacnetPoints = daqProperties.getBacnetPoints();
        Assert.notEmpty(bacnetPoints, "bacnetPoints must not be empty");
        Map<BacnetObject, Set<BacnetProperty>> objectProperties = bacnetPoints.stream().collect(
                Collectors.groupingBy(BacnetObjectPropertyWithKey::getBacnetObject,
                        Collectors.mapping(BacnetObjectPropertyWithKey::getBacnetProperty, Collectors.toSet()))
        );
        return new BacnetReadPropertyMultipleService(deviceInstance, objectProperties);
    }

//    public static DeviceStatus toDeviceStatus(DeviceDto deviceDto, ReadPropertyMultipleAck ack) {
//        MultiValueMap<BacnetObjectProperty, BacnetPropertyValue> valueMap = ack.flatMap();
//
//        List<DevicePointDto> devicePoints = deviceDto.getDevicePoints();
//        Map<String, Object> deviceStatus = CollectionUtils.newHashMap(devicePoints.size());
//        for (DevicePointDto devicePoint : devicePoints) {
//            if (!(devicePoint.getPointExtra() instanceof BacnetDevicePointExtra)) {
//                continue;
//            }
//            BacnetObjectProperty bop = ((BacnetDevicePointExtra) devicePoint.getPointExtra()).getBacnetObjectProperty();
//            Object pointValue = Optional.ofNullable(valueMap.get(bop))
//                    .map(in -> in.stream()
//                            .map(BacnetPropertyValue::getValue)
//                            .collect(Collectors.toList()))
//                    .map(CollectionUtils::oneOrMore)// on or more
//                    .orElse(null);// null represent the error
//            deviceStatus.put(devicePoint.getPointCode(), pointValue);
//        }
//        return new DeviceStatus(deviceDto.getDeviceCode(), deviceStatus, System.currentTimeMillis());
//    }
//
//    public static Optional<Integer> toDeviceInstance(DeviceDto deviceDto) {
//        return Optional.ofNullable(deviceDto)
//                .filter(in -> in.getDeviceExtra() instanceof BacnetDeviceExtra)
//                .map(in -> (BacnetDeviceExtra) in.getDeviceExtra())
//                .map(BacnetDeviceExtra::getDeviceInstance);
//    }
}
