package cn.ecosync.ibms.device;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DevicePointDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceMapper {
    public static DeviceDto mapWithPoints(Device device) {
        DeviceDto deviceDto = map(device);
        List<DevicePointDto> devicePoints = device.devicePoints().values().stream()
                .map(in -> new DevicePointDto(in.pointId().getPointCode(), in.pointProperties().getPointName(), in.pointProperties().getPointExtra()))
                .collect(Collectors.toList());
        deviceDto.setDevicePoints(devicePoints);
        return deviceDto;
    }

    public static DeviceDto map(Device device) {
        return new DeviceDto(
                device.deviceId().getDeviceCode(),
                device.deviceProperties().getDeviceName(),
                device.deviceProperties().getPath(),
                device.deviceProperties().getDescription(),
                device.enabled(),
                device.deviceProperties().getDeviceExtra(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
