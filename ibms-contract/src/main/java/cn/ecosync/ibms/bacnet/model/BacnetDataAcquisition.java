package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.*;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.ToString;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString(callSuper = true)
public class BacnetDataAcquisition extends DeviceDataAcquisition {
    private BacnetSchemas schemas;
    private List<BacnetDevice> devices;

    protected BacnetDataAcquisition() {
    }

    private BacnetDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId) {
        super(dataAcquisitionId, null);
    }

    public BacnetDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval, BacnetSchemas bacnetSchemas, List<BacnetDevice> bacnetDevices) {
        super(dataAcquisitionId, scrapeInterval);
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
    public BacnetDataAcquisition addDeviceReferences(List<Device> devices) {
        Stream<BacnetDevice> oldReferences = convertToReference(getDevices());
        Stream<BacnetDevice> newReferences = convertToReference(devices);
        List<BacnetDevice> references = Stream.concat(oldReferences, newReferences)
                .collect(Collectors.toList());
        return withDevices(references);
    }

    @Override
    public BacnetDataAcquisition removeDeviceReferences(List<Device> devices) {
        Map<DeviceId, BacnetDevice> oldReferences = convertToReference(getDevices())
                .collect(Collectors.toMap(Device::getDeviceId, Function.identity(), (foo, bar) -> bar, LinkedHashMap::new));
        convertToReference(devices)
                .map(Device::getDeviceId)
                .forEach(oldReferences::remove);
        return withDevices(new ArrayList<>(oldReferences.values()));
    }

    @Override
    public DeviceDataAcquisition toReference() {
        return new BacnetDataAcquisition(getDataAcquisitionId());
    }

    @Override
    public DeviceDataAcquisition withScrapeInterval(Long scrapeInterval) {
        return new BacnetDataAcquisition(getDataAcquisitionId(), scrapeInterval, getSchemas(), getDevices());
    }

    @Override
    public BacnetDataAcquisition withSchemas(DeviceSchemas schemas) {
        BacnetSchemas bacnetSchemas = Optional.ofNullable(schemas)
                .filter(BacnetSchemas.class::isInstance)
                .map(BacnetSchemas.class::cast)
                .orElse(null);
        return new BacnetDataAcquisition(getDataAcquisitionId(), getScrapeInterval(), bacnetSchemas, getDevices());
    }

    @Override
    public BacnetDataAcquisition withDevices(List<? extends Device> devices) {
        List<BacnetDevice> bacnetDevices = devices.stream()
                .map(BacnetDevice.class::cast)
                .collect(Collectors.toList());
        return new BacnetDataAcquisition(getDataAcquisitionId(), getScrapeInterval(), getSchemas(), bacnetDevices);
    }

    private Stream<BacnetDevice> convertToReference(List<? extends Device> devices) {
        return CollectionUtils.nullSafeOf(devices).stream()
                .map(BacnetDevice.class::cast)
                .map(BacnetDevice::toReference);
    }
}
