package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.model.Entity;
import cn.ecosync.ibms.util.StringUtils;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
public class DevicePoint extends Entity {
    private Device device;
    private DevicePointId devicePointId;
    private String pointName;
    private DevicePointProperties devicePointProperties;

    public static DevicePoint getReferenceOf(Device device, DevicePointId devicePointId) {
        DevicePoint devicePoint = new DevicePoint();
        devicePoint.device = device;
        devicePoint.devicePointId = devicePointId;
        return devicePoint;
    }

    protected DevicePoint() {
    }

    public DevicePoint(Device device, DevicePointId devicePointId, String pointName, DevicePointProperties devicePointProperties) {
        Assert.notNull(device, "device must not be null");
        Assert.notNull(devicePointId, "device point id can't be null");
        Assert.notNull(devicePointProperties, "device point properties can't be null");
        this.device = device;
        this.devicePointId = devicePointId;
        this.pointName = StringUtils.nullSafeOf(pointName);
        this.devicePointProperties = devicePointProperties;
    }

    public void setDevicePointProperties(DevicePointProperties devicePointProperties) {
        if (devicePointProperties != null) {
            this.devicePointProperties = devicePointProperties;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DevicePoint)) return false;
        DevicePoint that = (DevicePoint) o;
        return Objects.equals(device, that.device) && Objects.equals(devicePointId, that.devicePointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(device, devicePointId);
    }
}
