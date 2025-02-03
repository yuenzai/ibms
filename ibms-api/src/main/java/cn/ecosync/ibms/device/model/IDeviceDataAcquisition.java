package cn.ecosync.ibms.device.model;

public interface IDeviceDataAcquisition {
    Long getScrapeInterval();

    SynchronizationStateEnum getSynchronizationState();

    Iterable<? extends DeviceDataPoint> getDataPoints();

    enum SynchronizationStateEnum {
        UNSYNCHRONIZED,
        SYNCHRONIZING,
        SYNCHRONIZED,
    }
}
