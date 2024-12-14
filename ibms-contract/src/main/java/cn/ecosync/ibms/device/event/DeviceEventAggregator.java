package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@ToString
public class DeviceEventAggregator {
    @JsonDeserialize(contentAs = DeviceDto.class)
    private Set<DeviceModel> devices;

    protected DeviceEventAggregator() {
    }

    private DeviceEventAggregator(Set<DeviceModel> initializer) {
        this.devices = initializer != null ? initializer : new HashSet<>();
    }

    public void onApply(DeviceSavedEvent event) {
        event.device().ifPresent(devices::add);
    }

    public void onApply(DeviceRemovedEvent event) {
        event.device().ifPresent(devices::remove);
    }

    public static DeviceEventAggregator newInstance() {
        return DeviceEventAggregator.newInstance(new HashSet<>());
    }

    public static DeviceEventAggregator newInstance(Set<DeviceModel> initializer) {
        return new DeviceEventAggregator(initializer);
    }

    public static DeviceEventAggregator aggregator(Object ignored, AbstractDeviceEvent event, DeviceEventAggregator aggregator) {
        return event.apply(aggregator);
    }

    public static boolean canApply(Object ignored, AbstractDeviceEvent event) {
        return (event instanceof DeviceSavedEvent || event instanceof DeviceRemovedEvent) && event.device().isPresent();
    }

    public Set<DeviceModel> getDevices() {
        return CollectionUtils.nullSafeOf(devices);
    }
}
