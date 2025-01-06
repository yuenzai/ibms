package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@ToString(callSuper = true)
public class BacnetDataAcquisition extends DeviceDataAcquisition {
    private BacnetSchemas schemas;
    private List<BacnetDevice> devices;

    protected BacnetDataAcquisition() {
    }

    private BacnetDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId) {
        super(dataAcquisitionId);
    }

    public BacnetDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, BacnetSchemas bacnetSchemas, List<BacnetDevice> bacnetDevices) {
        super(dataAcquisitionId);
        this.schemas = bacnetSchemas;
        this.devices = bacnetDevices;
    }

    @Override
    public BacnetSchemas getSchemas() {
        return schemas;
    }

    @Override
    public List<BacnetDevice> getDevices() {
        return CollectionUtils.nullSafeOf(devices);
    }

    @Override
    public BacnetDataAcquisition addDeviceReferences(Collection<Device> devices) {
        Set<BacnetDevice> deviceReferences = getDevices().stream()
                .map(BacnetDevice::toReference)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        Set<BacnetDevice> newReferences = convertToReference(devices);
        deviceReferences.addAll(newReferences);
        return withDevices(new ArrayList<>(deviceReferences));
    }

    @Override
    public BacnetDataAcquisition removeDeviceReferences(Collection<Device> devices) {
        Set<BacnetDevice> deviceReferences = getDevices().stream()
                .map(BacnetDevice::toReference)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
        Set<BacnetDevice> newReferences = convertToReference(devices);
        deviceReferences.removeAll(newReferences);
        return withDevices(new ArrayList<>(deviceReferences));
    }

    @Override
    public DeviceDataAcquisition toReference() {
        return new BacnetDataAcquisition(getDataAcquisitionId());
    }

    @Override
    public BacnetDataAcquisition withSchemas(DeviceSchemas schemas) {
        BacnetSchemas bacnetSchemas = Optional.ofNullable(schemas)
                .filter(BacnetSchemas.class::isInstance)
                .map(BacnetSchemas.class::cast)
                .orElse(null);
        return new BacnetDataAcquisition(getDataAcquisitionId(), bacnetSchemas, getDevices());
    }

    @Override
    public BacnetDataAcquisition withDevices(List<Device> devices) {
        List<BacnetDevice> bacnetDevices = devices.stream()
                .filter(BacnetDevice.class::isInstance)
                .map(BacnetDevice.class::cast)
                .collect(Collectors.toList());
        return new BacnetDataAcquisition(getDataAcquisitionId(), getSchemas(), bacnetDevices);
    }

    private Set<BacnetDevice> convertToReference(Collection<Device> devices) {
        return CollectionUtils.nullSafeOf(devices).stream()
                .filter(BacnetDevice.class::isInstance)
                .map(BacnetDevice.class::cast)
                .map(BacnetDevice::toReference)
                .collect(Collectors.toSet());
    }
}
