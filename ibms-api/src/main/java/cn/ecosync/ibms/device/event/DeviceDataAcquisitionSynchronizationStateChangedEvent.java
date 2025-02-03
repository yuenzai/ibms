package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition.SynchronizationStateEnum;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class DeviceDataAcquisitionSynchronizationStateChangedEvent extends DeviceDataAcquisitionEvent {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private SynchronizationStateEnum synchronizationState;

    protected DeviceDataAcquisitionSynchronizationStateChangedEvent() {
    }

    public DeviceDataAcquisitionSynchronizationStateChangedEvent(DeviceDataAcquisitionId dataAcquisitionId, SynchronizationStateEnum synchronizationState) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        Assert.notNull(synchronizationState, "synchronizationState must not be null");
        this.dataAcquisitionId = dataAcquisitionId;
        this.synchronizationState = synchronizationState;
    }

    @Override
    public String eventKey() {
        return dataAcquisitionId.toString();
    }

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }

    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState;
    }
}
