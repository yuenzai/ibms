package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

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

    public DeviceGateway(DeviceGatewayId gatewayId, List<DeviceDataAcquisition> dataAcquisitions, SynchronizationStateEnum synchronizationState, Long previousSynchronizedDate) {
        this.gatewayId = gatewayId;
        this.dataAcquisitions = dataAcquisitions;
        this.synchronizationState = synchronizationState;
        this.previousSynchronizedDate = previousSynchronizedDate;
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
        return new DeviceGateway(getGatewayId(), dataAcquisitions, getSynchronizationState(), getPreviousSynchronizedDate());
    }

    public DeviceGateway withSynchronizationState(SynchronizationStateEnum synchronizationState) {
        if (synchronizationState == null || synchronizationState == UNSYNCHRONIZED) return this;
        switch (synchronizationState) {
            case SYNCHRONIZING:
                return new DeviceGateway(getGatewayId(), getDataAcquisitions(), synchronizationState, getPreviousSynchronizedDate());
            case SYNCHRONIZED:
                Long previousSynchronizedDate = getPreviousSynchronizedDate();
                if (synchronizationState != getSynchronizationState()) {
                    previousSynchronizedDate = System.currentTimeMillis();
                }
                return new DeviceGateway(getGatewayId(), getDataAcquisitions(), synchronizationState, previousSynchronizedDate);
        }
        throw new IllegalStateException("Unsupported synchronization state: " + synchronizationState);
    }
}
