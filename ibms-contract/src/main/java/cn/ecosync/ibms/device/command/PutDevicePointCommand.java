package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.dto.DevicePointDto;
import cn.ecosync.iframework.command.Command;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

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
