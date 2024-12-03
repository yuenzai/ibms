package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CollectDeviceMetricCommand {
    private DeviceId deviceId;

    protected CollectDeviceMetricCommand() {
    }

    public CollectDeviceMetricCommand(DeviceId deviceId) {
        this.deviceId = deviceId;
    }
}
