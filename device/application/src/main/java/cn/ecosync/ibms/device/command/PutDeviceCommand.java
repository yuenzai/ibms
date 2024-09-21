package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceProperties;
import cn.ecosync.ibms.system.model.DictionaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class PutDeviceCommand implements Command {
    @NotBlank
    private String deviceCode;
    @Setter
    @JsonIgnore
    private String networkId;
    private String deviceName;
    private String path;
    private String description;
    @Valid
    private DeviceProperties properties;

    public DeviceId toDeviceId() {
        return new DeviceId(deviceCode);
    }

    public DictionaryKey toNetworkId() {
        return new DictionaryKey(networkId);
    }
}
