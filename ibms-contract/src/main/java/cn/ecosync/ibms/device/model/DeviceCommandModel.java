package cn.ecosync.ibms.device.model;

public interface DeviceCommandModel extends DeviceModel {
    void update(String deviceName, String path, String description, DeviceExtra deviceExtra);
}
