package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class RemoveDevicePointCommand implements Command {
    @NotBlank
    private String deviceCode;
    @NotEmpty
    private List<String> pointCodes;

    public DeviceId toDeviceId() {
        return new DeviceId(this.deviceCode);
    }

    public List<DevicePointId> toDevicePointIds() {
        return CollectionUtils.nullSafeOf(this.pointCodes).stream()
                .map(DevicePointId::new)
                .collect(Collectors.toList());
    }
}
