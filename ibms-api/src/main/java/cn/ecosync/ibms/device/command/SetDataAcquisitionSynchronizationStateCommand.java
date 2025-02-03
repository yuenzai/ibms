package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition.SynchronizationStateEnum;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.ToString;

@ToString
public class SetDataAcquisitionSynchronizationStateCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private SynchronizationStateEnum synchronizationState;

    protected SetDataAcquisitionSynchronizationStateCommand() {
    }

    public SetDataAcquisitionSynchronizationStateCommand(DeviceDataAcquisitionId dataAcquisitionId, SynchronizationStateEnum synchronizationState) {
        this.dataAcquisitionId = dataAcquisitionId;
        this.synchronizationState = synchronizationState;
    }

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }

    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState;
    }
}
