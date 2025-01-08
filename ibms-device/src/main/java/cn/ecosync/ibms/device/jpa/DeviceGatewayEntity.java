package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.model.IDeviceGateway;
import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "device_gateway")
public class DeviceGatewayEntity extends ConcurrencySafeEntity implements IDeviceGateway {
    @Embedded
    private DeviceGatewayId gatewayId;
    @Column(name = "synchronization_state", nullable = false)
    private String synchronizationState;
    @Getter
    @Convert(converter = DeviceGatewayConverter.class)
    @Column(name = "gateway", nullable = false)
    private DeviceGateway gateway;

    protected DeviceGatewayEntity() {
    }

    public DeviceGatewayEntity(DeviceGateway gateway) {
        Assert.notNull(gateway, "gateway must not be null");
        this.gatewayId = gateway.getGatewayId();
        save(gateway);
    }

    @Override
    public List<DeviceDataAcquisition> getDataAcquisitions() {
        return gateway.getDataAcquisitions();
    }

    @Override
    public SynchronizationStateEnum getSynchronizationState() {
        return gateway.getSynchronizationState();
    }

    @Override
    public Long getPreviousSynchronizedDate() {
        return gateway.getPreviousSynchronizedDate();
    }

    public void save(DeviceGateway gateway) {
        Assert.notNull(gateway, "gateway must not be null");
        this.synchronizationState = gateway.getSynchronizationState().toString();
        this.gateway = gateway;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceGatewayEntity)) return false;
        DeviceGatewayEntity that = (DeviceGatewayEntity) o;
        return Objects.equals(this.gateway, that.gateway);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gateway);
    }
}
