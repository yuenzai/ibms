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
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceCode(device.deviceId().getDeviceCode());
        deviceDto.setDeviceName(device.deviceProperties().getDeviceName());
        deviceDto.setPath(device.deviceProperties().getPath());
        deviceDto.setDescription(device.deviceProperties().getDescription());
        deviceDto.setDeviceExtra(device.deviceProperties().getDeviceExtra());
        deviceDto.setEnabled(device.enabled());
        deviceDto.setDevicePoints(Collections.emptyList());
        deviceDto.setDeviceStatus(Collections.emptyList());
        return deviceDto;
    }
}
