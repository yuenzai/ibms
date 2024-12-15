package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Table(name = "device_daq")
public class DeviceDataAcquisition extends ConcurrencySafeEntity implements DeviceDataAcquisitionCommandModel {
    @Embedded
    private DeviceDataAcquisitionId daqId;
    @Column(name = "daq_properties", nullable = false)
    private DeviceDataAcquisitionProperties daqProperties;

    protected DeviceDataAcquisition() {
    }

    public DeviceDataAcquisition(DeviceDataAcquisitionId daqId, DeviceDataAcquisitionProperties daqProperties) {
        Assert.notNull(daqId, "daqId must not be null");
        Assert.notNull(daqProperties, "daqProperties must not be null");
        this.daqId = daqId;
        this.daqProperties = daqProperties;
    }

    @Override
    public void update(DeviceDataAcquisitionProperties daqProperties) {
        if (daqProperties == null) return;
        Assert.isTrue(Objects.equals(this.daqProperties.getClass(), daqProperties.getClass()), "daqProperties class not match!");
        this.daqProperties = daqProperties;
    }

    @Override
    public DeviceDataAcquisitionId getDaqId() {
        return daqId;
    }

    @Override
    public DeviceDataAcquisitionProperties getDaqProperties() {
        return daqProperties;
    }

    public static DeviceDataAcquisition newProbe() {
        return new DeviceDataAcquisition();
    }
}
