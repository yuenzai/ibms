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
//public class DeviceSchemaSavedEvent extends DeviceSchemaEvent {
//    public static final String EVENT_TYPE = "device-schema-saved-event";
//
//    @NotNull
//    @JsonUnwrapped
//    @JsonDeserialize(as = DeviceSchemaDto.class)
//    private DeviceSchemaQueryModel deviceSchema;
//
//    protected DeviceSchemaSavedEvent() {
//    }
//
//    private DeviceSchemaSavedEvent(DeviceSchemaQueryModel deviceSchema) {
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
//    public static DeviceSchemaSavedEvent newInstance(DeviceSchemaQueryModel deviceSchema) {
//        return new DeviceSchemaSavedEvent(deviceSchema);
//    }
//}
