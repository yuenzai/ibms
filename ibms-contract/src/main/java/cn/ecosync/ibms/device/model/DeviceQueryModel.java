package cn.ecosync.ibms.device.model;

public interface DeviceQueryModel {
    DeviceId getDeviceId();

    DeviceSchemaId getDeviceSchemaId();

    String getDeviceName();

    String getPath();

    String getDescription();

    DeviceExtra getDeviceExtra();
}
