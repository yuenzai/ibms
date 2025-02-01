package cn.ecosync.ibms.device.model;

public interface IDeviceDataAcquisition {
    Long getScrapeInterval();

    Iterable<? extends DeviceDataPoint> getDataPoints();
}
