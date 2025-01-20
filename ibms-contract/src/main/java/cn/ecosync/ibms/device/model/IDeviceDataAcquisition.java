package cn.ecosync.ibms.device.model;

public interface IDeviceDataAcquisition {
    Long getScrapeInterval();

    IDeviceSchemas getSchemas();

    Iterable<? extends IDevice> getDevices();
}
