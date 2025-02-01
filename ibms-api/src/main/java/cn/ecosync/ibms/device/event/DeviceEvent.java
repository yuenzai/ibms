//package cn.ecosync.ibms.device.event;
//
//import cn.ecosync.ibms.device.model.Device;
//import cn.ecosync.iframework.event.AbstractEvent;
//import com.fasterxml.jackson.annotation.JsonSubTypes;
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//
//import static cn.ecosync.ibms.Constants.AGGREGATE_TYPE_DEVICE;
//
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType", visible = true)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = DeviceSavedEvent.class, name = DeviceSavedEvent.EVENT_TYPE),
//        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = DeviceRemovedEvent.EVENT_TYPE),
//})
//public abstract class DeviceEvent extends AbstractEvent implements Device {
//    public DeviceEvent() {
//        super(AGGREGATE_TYPE_DEVICE);
//    }
//
//    public abstract DeviceEventAggregator apply(DeviceEventAggregator aggregator);
//}
