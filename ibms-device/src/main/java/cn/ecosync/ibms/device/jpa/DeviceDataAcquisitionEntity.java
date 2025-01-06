package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.*;
import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "device_daq")
public class DeviceDataAcquisitionEntity extends ConcurrencySafeEntity implements IDeviceDataAcquisition {
    @Embedded
    private DeviceDataAcquisitionId dataAcquisitionId;
    @Getter
    @Convert(converter = DeviceDataAcquisitionConverter.class)
    @Column(name = "data_acquisition", nullable = false, updatable = false)
    private DeviceDataAcquisition dataAcquisition;

    protected DeviceDataAcquisitionEntity() {
    }

    public DeviceDataAcquisitionEntity(DeviceDataAcquisition dataAcquisition) {
        Assert.notNull(dataAcquisition, "dataAcquisition must not be null");
        this.dataAcquisitionId = dataAcquisition.getDataAcquisitionId();
        this.dataAcquisition = dataAcquisition;
    }

    @Override
    public DeviceSchemas getSchemas() {
        return dataAcquisition.getSchemas();
    }

    @Override
    public List<? extends Device> getDevices() {
        return dataAcquisition.getDevices();
    }

    public void save(DeviceDataAcquisition dataAcquisition) {
        Assert.notNull(dataAcquisition, "dataAcquisition must not be null");
        this.dataAcquisition = dataAcquisition;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataAcquisitionEntity)) return false;
        DeviceDataAcquisitionEntity other = (DeviceDataAcquisitionEntity) o;
        return Objects.equals(this.dataAcquisition, other.dataAcquisition);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dataAcquisition);
    }
}
