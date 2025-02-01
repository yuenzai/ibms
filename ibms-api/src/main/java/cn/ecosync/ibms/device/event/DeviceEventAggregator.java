//package cn.ecosync.ibms.device.event;
//
//import cn.ecosync.ibms.device.dto.DeviceDtoBak;
//import cn.ecosync.ibms.device.model.IDevice;
//import cn.ecosync.iframework.util.CollectionUtils;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import lombok.ToString;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//@ToString
//public class DeviceEventAggregator {
//    @JsonDeserialize(contentAs = DeviceDtoBak.class)
//    private Set<IDevice> devices;
//
//    protected DeviceEventAggregator() {
//    }
//
//    private DeviceEventAggregator(Set<IDevice> initializer) {
//        this.devices = initializer != null ? initializer : new HashSet<>();
//    }
//
//    public void onApply(DeviceSavedEvent event) {
//        devices.add(event);
//    }
//
//    public void onApply(DeviceRemovedEvent event) {
//        devices.remove(event);
//    }
//
//    public static DeviceEventAggregator newInstance() {
//        return DeviceEventAggregator.newInstance(new HashSet<>());
//    }
//
//    public static DeviceEventAggregator newInstance(Set<IDevice> initializer) {
//        return new DeviceEventAggregator(initializer);
//    }
//
//    public static DeviceEventAggregator aggregator(Object ignored, DeviceEvent event, DeviceEventAggregator aggregator) {
//        return event.apply(aggregator);
//    }
//
//    public Collection<IDevice> getDevices() {
//        return CollectionUtils.nullSafeOf(devices);
//    }
//}
