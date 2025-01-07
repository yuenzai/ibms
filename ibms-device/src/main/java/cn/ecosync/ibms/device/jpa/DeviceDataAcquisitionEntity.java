package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.*;
import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "device_daq")
public class DeviceDataAcquisitionEntity extends ConcurrencySafeEntity implements IDeviceDataAcquisition {
    @Embedded
    private DeviceDataAcquisitionId dataAcquisitionId;
    @Getter
    @Convert(converter = DeviceDataAcquisitionConverter.class)
    @Column(name = "data_acquisition", nullable = false, updatable = false)
    private DeviceDataAcquisition dataAcquisition;

    @ManyToMany
    @JoinTable(name = "rel_daq_device",
            joinColumns = @JoinColumn(name = "daq_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "device_id", nullable = false, updatable = false))
    private Set<DeviceEntity> deviceEntities = new LinkedHashSet<>();

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

    public void add(Collection<DeviceEntity> deviceEntities) {
        if (CollectionUtils.isEmpty(deviceEntities)) return;
        for (DeviceEntity deviceEntity : deviceEntities) {
            if (this.deviceEntities.add(deviceEntity)) {
                deviceEntity.add(this);
            }
        }
        List<Device> devices = deviceEntities.stream()
                .map(DeviceEntity::getDevice)
                .collect(Collectors.toList());
        this.dataAcquisition = this.dataAcquisition.addDeviceReferences(devices);
    }

    public void remove(Collection<DeviceEntity> deviceEntities) {
        if (CollectionUtils.isEmpty(deviceEntities)) return;
        for (DeviceEntity deviceEntity : deviceEntities) {
            if (this.deviceEntities.remove(deviceEntity)) {
                deviceEntity.remove(this);
            }
        }
        List<Device> devices = deviceEntities.stream()
                .map(DeviceEntity::getDevice)
                .collect(Collectors.toList());
        this.dataAcquisition = this.dataAcquisition.removeDeviceReferences(devices);
    }

    public void clear() {
        for (DeviceEntity deviceEntity : deviceEntities) {
            deviceEntity.remove(this);
        }
        deviceEntities.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataAcquisitionEntity)) return false;
        DeviceDataAcquisitionEntity that = (DeviceDataAcquisitionEntity) o;
        return Objects.equals(this.dataAcquisition, that.dataAcquisition);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dataAcquisition);
    }
}
