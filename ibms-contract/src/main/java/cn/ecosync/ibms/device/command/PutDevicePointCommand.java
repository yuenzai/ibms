package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DevicePointDto;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@ToString
public class PutDevicePointCommand implements Command {
    @NotBlank
    private String deviceCode;

    @Valid
    @NotEmpty
    private List<DevicePointDto> devicePoints;

    public List<DevicePointDto> getDevicePoints() {
        return CollectionUtils.nullSafeOf(devicePoints);
    }
}
