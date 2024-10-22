package cn.ecosync.ibms.bacnet;

import cn.ecosync.ibms.bacnet.model.*;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DevicePointDto;
import cn.ecosync.ibms.device.model.DeviceStatus;
import cn.ecosync.ibms.util.CollectionUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BacnetMapper {
    public static BacnetReadPropertyMultiple toReadPropertyMultipleService(DeviceDto device) {
        Assert.notNull(device, "deviceDto must not be null");
        // 设备配置属性
        BacnetDeviceExtra deviceProperties = (BacnetDeviceExtra) device.getDeviceExtra();
        // 点位配置属性
        List<BacnetDevicePointExtra> devicePoints = device.getDevicePoints().stream()
                .filter(in -> in.getPointExtra() instanceof BacnetDevicePointExtra)
                .map(in -> (BacnetDevicePointExtra) in.getPointExtra())
                .collect(Collectors.toList());
        Assert.notEmpty(devicePoints, "devicePoints must not be empty");
        Map<BacnetObject, List<BacnetProperty>> objectProperties = devicePoints.stream()
                .collect(Collectors.groupingBy(BacnetDevicePointExtra::toBacnetObject, Collectors.mapping(BacnetDevicePointExtra::toBacnetProperty, Collectors.toList())));
        return new BacnetReadPropertyMultiple(deviceProperties.getDeviceInstance(), objectProperties);
    }

    public static DeviceStatus toDeviceStatus(DeviceDto deviceDto, ReadPropertyMultipleAck ack) {
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

    public static Optional<Integer> toDeviceInstance(DeviceDto deviceDto) {
        return Optional.ofNullable(deviceDto)
                .filter(in -> in.getDeviceExtra() instanceof BacnetDeviceExtra)
                .map(in -> (BacnetDeviceExtra) in.getDeviceExtra())
                .map(BacnetDeviceExtra::getDeviceInstance);
    }
}
