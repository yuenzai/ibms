package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReadDeviceMetricCommand {
    private DeviceId deviceId;

    protected ReadDeviceMetricCommand() {
    }

    public ReadDeviceMetricCommand(DeviceId deviceId) {
        this.deviceId = deviceId;
    }
}
