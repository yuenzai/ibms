package cn.ecosync.ibms.orchestration;

import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.dto.DevicePointDto;
import cn.ecosync.ibms.device.dto.DeviceStatus;
import cn.ecosync.iframework.util.CollectionUtils;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BacnetMapper {
    public static BacnetReadPropertyMultipleService toReadPropertyMultipleService(DeviceDto device) {
        Assert.notNull(device, "deviceDto must not be null");
        // 设备配置属性
        BacnetDeviceExtra deviceProperties = (BacnetDeviceExtra) device.getDeviceExtra();
        // 点位配置属性
        List<BacnetDevicePointExtra> devicePoints = device.getDevicePoints().stream()
                .filter(in -> in.getPointExtra() instanceof BacnetDevicePointExtra)
                .map(in -> (BacnetDevicePointExtra) in.getPointExtra())
                .collect(Collectors.toList());
        Assert.notEmpty(devicePoints, "devicePoints must not be empty");
        Map<BacnetObject, Set<BacnetProperty>> objectProperties = devicePoints.stream()
                .collect(Collectors.groupingBy(BacnetDevicePointExtra::toBacnetObject, Collectors.mapping(BacnetDevicePointExtra::toBacnetProperty, Collectors.toSet())));
        return new BacnetReadPropertyMultipleService(deviceProperties.getDeviceInstance(), objectProperties);
    }

    public static DeviceStatus toDeviceStatus(DeviceDto deviceDto, ReadPropertyMultipleAck ack) {
        MultiValueMap<BacnetObjectProperty, BacnetPropertyValue> valueMap = ack.flatMap();

        List<DevicePointDto> devicePoints = deviceDto.getDevicePoints();
        Map<String, Object> deviceStatus = CollectionUtils.newHashMap(devicePoints.size());
        for (DevicePointDto devicePoint : devicePoints) {
            if (!(devicePoint.getPointExtra() instanceof BacnetDevicePointExtra)) {
                continue;
            }
            BacnetObjectProperty bop = ((BacnetDevicePointExtra) devicePoint.getPointExtra()).getBacnetObjectProperty();
            Object pointValue = Optional.ofNullable(valueMap.get(bop))
                    .map(in -> in.stream()
                            .map(BacnetPropertyValue::getValue)
                            .collect(Collectors.toList()))
                    .map(CollectionUtils::oneOrMore)// on or more
                    .orElse(null);// null represent the error
            deviceStatus.put(devicePoint.getPointCode(), pointValue);
        }
        return new DeviceStatus(deviceDto.getDeviceCode(), deviceStatus, System.currentTimeMillis());
    }

    public static Optional<Integer> toDeviceInstance(DeviceDto deviceDto) {
        return Optional.ofNullable(deviceDto)
                .filter(in -> in.getDeviceExtra() instanceof BacnetDeviceExtra)
                .map(in -> (BacnetDeviceExtra) in.getDeviceExtra())
                .map(BacnetDeviceExtra::getDeviceInstance);
    }
}
