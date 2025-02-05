package cn.ecosync.ibms.device.model;

import java.util.List;

public interface IDeviceGateway {
    List<? extends DeviceDataAcquisition> getDataAcquisitions();

    SynchronizationStateEnum getSynchronizationState();

    Long getPreviousSynchronizedDate();

    enum SynchronizationStateEnum {
        UNSYNCHRONIZED,
        SYNCHRONIZING,
        SYNCHRONIZED,
    }
}
