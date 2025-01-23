package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.dto.DeviceProbe;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.model.IDevice;
import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import cn.ecosync.iframework.util.StringUtils;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Table(name = "device")
public class DeviceEntity extends ConcurrencySafeEntity implements IDevice {
    @Embedded
    private DeviceId deviceId;
    @Embedded
    private DeviceSchemasId schemasId;
    @Column(name = "device_name", nullable = false)
    private String deviceName;
    @Getter
    @Convert(converter = DeviceConverter.class)
    @Column(name = "device", nullable = false)
    private Device device;

//    @ManyToMany(mappedBy = "deviceEntities")
//    private Set<DeviceDataAcquisitionEntity> dataAcquisitionEntities = new LinkedHashSet<>();

    protected DeviceEntity() {
    }

    public DeviceEntity(Device device) {
        Assert.notNull(device, "device must not be null");
        this.deviceId = device.getDeviceId();
        this.schemasId = device.getSchemasId();
        this.deviceName = StringUtils.nullSafeOf(device.getDeviceName());
        this.device = device;
    }

    public void save(Device device) {
        Assert.notNull(device, "device must not be null");
        Assert.isTrue(Objects.equals(getDevice().getSchemasId(), device.getSchemasId()), "");
        this.deviceName = StringUtils.nullSafeOf(device.getDeviceName());
        this.device = device;
    }

//    public void clear() {
//        dataAcquisitionEntities.clear();
//    }
//
//    void add(DeviceDataAcquisitionEntity dataAcquisitionEntity) {
//        Assert.notNull(dataAcquisitionEntity, "dataAcquisitionEntity must not be null");
//        dataAcquisitionEntities.add(dataAcquisitionEntity);
//    }
//
//    void remove(DeviceDataAcquisitionEntity dataAcquisitionEntity) {
//        Assert.notNull(dataAcquisitionEntity, "dataAcquisitionEntity must not be null");
//        dataAcquisitionEntities.remove(dataAcquisitionEntity);
//    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceEntity)) return false;
        DeviceEntity that = (DeviceEntity) o;
        return Objects.equals(this.deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceId);
    }

    public static DeviceEntity newProbe(DeviceProbe probe) {
        DeviceEntity probeEntity = new DeviceEntity();
        probeEntity.schemasId = probe.toSchemasId();
        probeEntity.deviceName = probe.getDeviceName();
        return probeEntity;
    }
}
