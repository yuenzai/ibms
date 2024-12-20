package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.domain.IdentifiedValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.util.Assert;

@Entity
@Table(name = "device_daq_deleted")
public class DeviceDataAcquisitionDeleted extends IdentifiedValueObject implements DeviceDataAcquisitionModel {
    @Embedded
    private DeviceDataAcquisitionId daqId;
    @Column(name = "daq_properties", nullable = false)
    private DeviceDataAcquisitionProperties daqProperties;

    protected DeviceDataAcquisitionDeleted() {
    }

    public DeviceDataAcquisitionDeleted(DeviceDataAcquisitionModel daq) {
        this(daq.getDaqId(), daq.getDaqProperties());
    }

    public DeviceDataAcquisitionDeleted(DeviceDataAcquisitionId daqId, DeviceDataAcquisitionProperties daqProperties) {
        Assert.notNull(daqId, "daqId must not be null");
        Assert.notNull(daqProperties, "daqProperties must not be null");
        this.daqId = daqId;
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
}
