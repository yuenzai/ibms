package cn.ecosync.ibms.device;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.dto.DevicePointDto;
import cn.ecosync.ibms.device.model.Device;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceMapper {
    public static DeviceDto mapOf(Device device) {
        List<DevicePointDto> devicePoints = device.devicePoints().values().stream()
                .map(in -> new DevicePointDto(in.pointId(), in.pointProperties()))
                .collect(Collectors.toList());
        return new DeviceDto(device.deviceId(), device.deviceProperties(), device.enabled(), devicePoints, Collections.emptyList());
    }
}
