//package cn.ecosync.ibms.device.jpa;
//
//import cn.ecosync.ibms.device.model.DeviceDataPoint;
//import cn.ecosync.ibms.device.model.DeviceDataPointId;
//import cn.ecosync.iframework.domain.Entity;
//import jakarta.persistence.Column;
//import jakarta.persistence.Convert;
//import jakarta.persistence.Embedded;
//import jakarta.persistence.Table;
//import lombok.Getter;
//import org.springframework.util.Assert;
//
//import java.util.Objects;
//
//@jakarta.persistence.Entity
//@Table(name = "device_data_point")
//public class DeviceDataPointEntity extends Entity {
//    @Embedded
//    private DeviceDataPointId dataPointId;
//    @Getter
//    @Convert(converter = DeviceDataPointConverter.class)
//    @Column(name = "device_data_point", nullable = false)
//    private DeviceDataPoint dataPoint;
//
//    protected DeviceDataPointEntity() {
//    }
//
//    public DeviceDataPointEntity(DeviceDataPoint dataPoint) {
//        Assert.notNull(dataPoint, "dataPoint must not be null");
//        this.dataPointId = dataPoint.getDataPointId();
//        this.dataPoint = dataPoint;
//    }
//
//    public void save(DeviceDataPoint dataPoint) {
//        this.dataPoint = dataPoint;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof DeviceDataPointEntity)) return false;
//        DeviceDataPointEntity that = (DeviceDataPointEntity) o;
//        return Objects.equals(this.dataPointId, that.dataPointId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(dataPointId);
//    }
//}
