package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.ToString;

import java.util.List;

@ToString
public class ReloadPrometheusConfigurationCommand implements Command {
    private List<DeviceDataAcquisition> dataAcquisitions;

    protected ReloadPrometheusConfigurationCommand() {
    }

    public ReloadPrometheusConfigurationCommand(List<DeviceDataAcquisition> dataAcquisitions) {
        this.dataAcquisitions = dataAcquisitions;
    }

    public List<DeviceDataAcquisition> getDataAcquisitions() {
        return CollectionUtils.nullSafeOf(dataAcquisitions);
    }
}
