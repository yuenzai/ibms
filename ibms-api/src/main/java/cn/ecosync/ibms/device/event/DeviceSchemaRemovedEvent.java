//package cn.ecosync.ibms.device.event;
//
//import cn.ecosync.ibms.device.model.DeviceSchemaDto;
//import cn.ecosync.ibms.device.model.DeviceSchemaQueryModel;
//import com.fasterxml.jackson.annotation.JsonUnwrapped;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import jakarta.validation.constraints.NotNull;
//import lombok.ToString;
//import org.springframework.util.Assert;
//
//import java.util.Optional;
//
//@ToString
//public class DeviceSchemaRemovedEvent extends DeviceSchemaEvent {
//    public static final String EVENT_TYPE = "device-schema-removed-event";
//
//    @NotNull
//    @JsonUnwrapped
//    @JsonDeserialize(as = DeviceSchemaDto.class)
//    private DeviceSchemaQueryModel deviceSchema;
//
//    protected DeviceSchemaRemovedEvent() {
//    }
//
//    private DeviceSchemaRemovedEvent(DeviceSchemaQueryModel deviceSchema) {
//        Assert.notNull(deviceSchema, "deviceSchema must not be null");
//        this.deviceSchema = deviceSchema;
//    }
//
//    @Override
//    public String eventKey() {
//        return deviceSchema.getSchemaId().toString();
//    }
//
//    @Override
//    public Optional<DeviceSchemaQueryModel> deviceSchema() {
//        return Optional.of(deviceSchema);
//    }
//
//    public static DeviceSchemaRemovedEvent newInstance(DeviceSchemaQueryModel deviceSchema) {
//        return new DeviceSchemaRemovedEvent(deviceSchema);
//    }
//}
