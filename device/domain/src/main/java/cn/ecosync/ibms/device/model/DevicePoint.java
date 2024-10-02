package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.jpa.DevicePointPropertiesJpaConverter;
import cn.ecosync.ibms.model.Entity;
import cn.ecosync.ibms.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Objects;

@Getter
@javax.persistence.Entity
@Table(name = "device_point")
public class DevicePoint extends Entity {
    @JoinColumn(name = "device_id", nullable = false, updatable = false)
    @ManyToOne(targetEntity = Device.class)
    @Setter
    private Device device;
    @Embedded
    private DevicePointId pointId;
    @Column(name = "point_name", nullable = false)
    private String pointName;
    @Convert(converter = DevicePointPropertiesJpaConverter.class)
    @Column(name = "properties", nullable = false)
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
