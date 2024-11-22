package cn.ecosync.ibms.domain.device;

import cn.ecosync.iframework.domain.IdentifiedValueObject;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "device_point")
public class DevicePoint extends IdentifiedValueObject {
    @ManyToOne(targetEntity = Device.class)
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    private Device device;
    @Embedded
    private DevicePointId pointId;
    @Embedded
    private DevicePointProperties pointProperties;

    protected DevicePoint() {
    }

    public DevicePoint(Device device, DevicePointId pointId, DevicePointProperties pointProperties) {
        Assert.notNull(device, "device can not be null");
        Assert.notNull(pointId, "pointId can not be null");
        Assert.notNull(pointProperties, "pointProperties can not be null");
        this.device = device;
        this.pointId = pointId;
        this.pointProperties = pointProperties;
    }

    public void update(DevicePointProperties pointProperties) {
        if (pointProperties != null) {
            this.pointProperties = pointProperties;
        }
    }

    public void device(Device device) {
        this.device = device;
    }

    public DevicePointId pointId() {
        return pointId;
    }

    public DevicePointProperties pointProperties() {
        return pointProperties;
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
