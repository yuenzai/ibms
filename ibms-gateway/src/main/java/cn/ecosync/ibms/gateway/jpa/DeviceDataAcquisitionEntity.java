package cn.ecosync.ibms.gateway.jpa;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.jpa.JpaEntity;
import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Table(name = "gateway_data_acquisition")
public class DeviceDataAcquisitionEntity extends JpaEntity<Integer> {
    @Embedded
    private DeviceDataAcquisitionId dataAcquisitionId;
    @Convert(converter = DeviceDataAcquisitionConverter.class)
    @Column(name = "payload", nullable = false)
    private DeviceDataAcquisition payload;

    protected DeviceDataAcquisitionEntity() {
    }

    public DeviceDataAcquisitionEntity(DeviceDataAcquisition payload) {
        Assert.notNull(payload, "DataAcquisition payload must not be null");
        this.dataAcquisitionId = payload.getDataAcquisitionId();
        this.payload = payload;
    }

    public DeviceDataAcquisition getPayload() {
        return payload;
    }

    public void setPayload(DeviceDataAcquisition payload) {
        this.payload = payload;
    }

    @Override
    protected Integer id() {
        return super.id();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataAcquisitionEntity)) return false;
        DeviceDataAcquisitionEntity that = (DeviceDataAcquisitionEntity) o;
        return Objects.equals(this.dataAcquisitionId, that.dataAcquisitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dataAcquisitionId);
    }
}
