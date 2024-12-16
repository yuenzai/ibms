package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.command.Command;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public abstract class CreateDeviceCommand implements Command {
    private DeviceDataAcquisitionId daqId;

    protected CreateDeviceCommand() {
    }

    public abstract List<DeviceModel> newDevices();
}
