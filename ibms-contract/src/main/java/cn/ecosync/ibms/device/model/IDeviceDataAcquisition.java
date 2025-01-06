package cn.ecosync.ibms.device.model;

public interface IDeviceDataAcquisition {
    IDeviceSchemas getSchemas();

    Iterable<? extends IDevice> getDevices();
}
