package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointValue;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class DeviceStatusDto extends DeviceDto {
    private List<DevicePointStatusDto> properties;

    protected DeviceStatusDto() {
    }

    public DeviceStatusDto(Device device, Map<DevicePoint, DevicePointValue> properties) {
        super(device);
        this.properties = CollectionUtils.nullSafeOf(properties).entrySet().stream()
                .map(in -> new DevicePointStatusDto(in.getKey(), in.getValue()))
                .collect(Collectors.toList());
    }
}
