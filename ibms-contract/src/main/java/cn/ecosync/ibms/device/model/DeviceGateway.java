package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum.SYNCHRONIZED;
import static cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum.UNSYNCHRONIZED;

@Getter
@ToString
public class DeviceGateway implements IDeviceGateway {
    @Valid
    @NotNull
    @JsonUnwrapped
    private DeviceGatewayId gatewayId;
    private List<DeviceDataAcquisition> dataAcquisitions;
    private SynchronizationStateEnum synchronizationState;
    private Long previousSynchronizedDate;

    protected DeviceGateway() {
    }

    public DeviceGateway(DeviceGatewayId gatewayId, List<DeviceDataAcquisition> dataAcquisitions, SynchronizationStateEnum synchronizationState) {
        this.gatewayId = gatewayId;
        this.dataAcquisitions = dataAcquisitions;
        this.synchronizationState = synchronizationState;
    }

    @Override
    public List<DeviceDataAcquisition> getDataAcquisitions() {
        return CollectionUtils.nullSafeOf(dataAcquisitions);
    }

    @Override
    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState != null ? synchronizationState : UNSYNCHRONIZED;
    }

    @Override
    public Long getPreviousSynchronizedDate() {
        return previousSynchronizedDate;
    }

    public DeviceGateway withDataAcquisitions(List<DeviceDataAcquisition> dataAcquisitions) {
        return new DeviceGateway(getGatewayId(), dataAcquisitions, getSynchronizationState());
    }

    public DeviceGateway withSynchronizationState(SynchronizationStateEnum synchronizationState) {
        DeviceGateway deviceGateway = new DeviceGateway(getGatewayId(), getDataAcquisitions(), synchronizationState);
        deviceGateway.previousSynchronizedDate = synchronizationState == SYNCHRONIZED && getSynchronizationState() != synchronizationState ?
                System.currentTimeMillis() : getPreviousSynchronizedDate();
        return deviceGateway;
    }
}
