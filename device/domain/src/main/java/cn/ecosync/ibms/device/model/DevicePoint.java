package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.model.Entity;
import cn.ecosync.ibms.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
public class DevicePoint extends Entity {
    @Setter
    private Device device;
    private DevicePointId pointId;
    private String pointName;
    private DevicePointProperties pointProperties;

    protected DevicePoint() {
    }

    public DevicePoint(Device device, DevicePointId pointId, String pointName, DevicePointProperties pointProperties) {
        Assert.notNull(device, "device must not be null");
        Assert.notNull(pointId, "device point id can't be null");
        Assert.notNull(pointProperties, "device point properties can't be null");
        this.device = device;
        this.pointId = pointId;
        this.pointName = StringUtils.nullSafeOf(pointName);
        this.pointProperties = pointProperties;
    }

    public void setPointProperties(DevicePointProperties pointProperties) {
        if (pointProperties != null) {
            this.pointProperties = pointProperties;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DevicePoint)) return false;
        DevicePoint that = (DevicePoint) o;
        return Objects.equals(pointId, that.pointId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pointId);
    }
}
