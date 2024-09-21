package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceProperties;
import cn.ecosync.ibms.system.model.DictionaryKey;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class AddDeviceCommand implements Command {
    @NotBlank
    private String networkId;
    @NotBlank
    private String deviceCode;
    private String deviceName;
    private String path;
    private String description;
    @Valid
    @NotNull(message = "device properties must not be null")
    private DeviceProperties properties;

    public DeviceId toDeviceId() {
        return new DeviceId(deviceCode);
    }

    public DictionaryKey toNetworkId() {
        return new DictionaryKey(networkId);
    }

    @Override
    public void validate() {
//        Class<?> devicePropertiesType = Optional.ofNullable(properties.type())
//                .map(DeviceTypeEnum::of)
//                .map(DeviceTypeEnum::getDevicePropertiesType)
//                .orElseThrow(() -> new IllegalArgumentException("properties type not found"));
//        if (!devicePropertiesType.isAssignableFrom(properties.getClass())) {
//            throw new IllegalArgumentException("type not match");
//        }
    }
}
