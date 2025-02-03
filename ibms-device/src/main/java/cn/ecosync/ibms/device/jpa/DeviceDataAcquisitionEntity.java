package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Table(name = "device_daq")
public class DeviceDataAcquisitionEntity extends ConcurrencySafeEntity {
    @Embedded
    private DeviceDataAcquisitionId dataAcquisitionId;
    @Getter
    @Convert(converter = DeviceDataAcquisitionConverter.class)
    @Column(name = "device_daq", nullable = false)
    private DeviceDataAcquisition dataAcquisition;

//    @ManyToMany
//    @JoinTable(name = "rel_daq_device",
//            joinColumns = @JoinColumn(name = "daq_id", nullable = false),
//            inverseJoinColumns = @JoinColumn(name = "device_id", nullable = false))
//    private Set<DeviceEntity> deviceEntities = new LinkedHashSet<>();

    protected DeviceDataAcquisitionEntity() {
    }

    public DeviceDataAcquisitionEntity(DeviceDataAcquisition dataAcquisition) {
        Assert.notNull(dataAcquisition, "dataAcquisition must not be null");
        this.dataAcquisitionId = dataAcquisition.getDataAcquisitionId();
        this.dataAcquisition = dataAcquisition;
    }

    public void save(DeviceDataAcquisition dataAcquisition) {
        this.dataAcquisition = dataAcquisition;
    }

//    public void add(Collection<DeviceEntity> deviceEntities) {
//        if (CollectionUtils.isEmpty(deviceEntities)) return;
//        for (DeviceEntity deviceEntity : deviceEntities) {
//            if (this.deviceEntities.add(deviceEntity)) {
//                deviceEntity.add(this);
//            }
//        }
//        List<Device> devices = deviceEntities.stream()
//                .map(DeviceEntity::getDevice)
//                .collect(Collectors.toList());
//        this.dataAcquisition = this.dataAcquisition.addDeviceReferences(devices);
//    }
//
//    public void remove(Collection<DeviceEntity> deviceEntities) {
//        if (CollectionUtils.isEmpty(deviceEntities)) return;
//        for (DeviceEntity deviceEntity : deviceEntities) {
//            if (this.deviceEntities.remove(deviceEntity)) {
//                deviceEntity.remove(this);
//            }
//        }
//        List<Device> devices = deviceEntities.stream()
//                .map(DeviceEntity::getDevice)
//                .collect(Collectors.toList());
//        this.dataAcquisition = this.dataAcquisition.removeDeviceReferences(devices);
//    }
//
//    public void clear() {
//        for (DeviceEntity deviceEntity : deviceEntities) {
//            deviceEntity.remove(this);
//        }
//        deviceEntities.clear();
//    }

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
