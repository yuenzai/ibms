package cn.ecosync.ibms.device.model;

public interface DeviceModel {
    DeviceId getDeviceId();

    DeviceDataAcquisitionId getDaqId();

    String getDeviceName();

    String getPath();

    String getDescription();

    DeviceExtra getDeviceExtra();
}
