package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class RemoveDevicePointCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceId deviceId;
    private List<String> pointCodes;

    public List<DevicePointId> toDevicePointIds() {
        return CollectionUtils.nullSafeOf(this.pointCodes).stream()
                .map(DevicePointId::new)
                .collect(Collectors.toList());
    }
}
