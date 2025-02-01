//package cn.ecosync.ibms.device.event;
//
//import cn.ecosync.ibms.device.model.DeviceSchemaQueryModel;
//import cn.ecosync.iframework.event.AbstractEvent;
//import com.fasterxml.jackson.annotation.JsonSubTypes;
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//
//import java.util.Optional;
//
//import static cn.ecosync.ibms.Constants.AGGREGATE_TYPE_DEVICE_SCHEMA;
//
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType", visible = true)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = DeviceSchemaSavedEvent.class, name = DeviceSchemaSavedEvent.EVENT_TYPE),
//        @JsonSubTypes.Type(value = DeviceSchemaRemovedEvent.class, name = DeviceSchemaRemovedEvent.EVENT_TYPE),
//})
//public abstract class DeviceSchemaEvent extends AbstractEvent {
//    public DeviceSchemaEvent() {
//        super(AGGREGATE_TYPE_DEVICE_SCHEMA);
//    }
//
//    public abstract Optional<DeviceSchemaQueryModel> deviceSchema();
//}
