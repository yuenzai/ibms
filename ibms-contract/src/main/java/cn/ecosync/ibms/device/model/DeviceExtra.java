package cn.ecosync.ibms.device.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import static cn.ecosync.ibms.device.model.DeviceExtra.BACnet;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BACnet.class, name = "BACNET"),
})
public interface DeviceExtra {
    @Getter
    @ToString
    class BACnet implements DeviceExtra {
        @NotNull
        private Integer deviceInstance;

        protected BACnet() {
        }
    }

    @ToString
    class Null implements DeviceExtra {
        public static final Null INSTANCE = new Null();

        private Null() {
        }
    }
}
