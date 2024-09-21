package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.Constants;
import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceProperties;
import cn.ecosync.ibms.system.model.SystemDictionaryKey;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class PutDeviceCommand<T extends DeviceProperties> implements Command {
    @NotBlank
    private String deviceCode;
    @NotBlank(groups = Constants.Create.class)
    private String networkId;
    private String deviceName;
    private String path;
    private String description;
    @Valid
    private T properties;

    protected PutDeviceCommand() {
    }

    public DeviceId toDeviceId() {
        return new DeviceId(deviceCode);
    }

    public SystemDictionaryKey toNetworkId() {
        return new SystemDictionaryKey(networkId);
    }
}