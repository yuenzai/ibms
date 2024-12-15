package cn.ecosync.ibms.device.model;

public interface DeviceModel {
    DeviceId getDeviceId();

    DeviceDataAcquisitionId getDaqId();

    DeviceProperties getDeviceProperties();
}
