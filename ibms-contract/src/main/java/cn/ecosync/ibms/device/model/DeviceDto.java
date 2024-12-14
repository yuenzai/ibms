package cn.ecosync.ibms.device.model;

import lombok.ToString;

import java.util.Objects;

@ToString
public class DeviceDto implements DeviceModel {
    private DeviceId deviceId;
    private DeviceDataAcquisitionId daqId;
    private String deviceName;
    private String path;
    private String description;
    private DeviceExtra deviceExtra;

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public DeviceDataAcquisitionId getDaqId() {
        return daqId;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public DeviceExtra getDeviceExtra() {
        return deviceExtra;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDto)) return false;
        DeviceDto that = (DeviceDto) o;
        return Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceId);
    }
}
