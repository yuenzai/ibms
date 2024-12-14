package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.dto.BacnetObjectProperty;
import cn.ecosync.iframework.util.CollectionDelegate;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.Valid;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static cn.ecosync.ibms.device.model.DeviceDataAcquisitionProperties.BACnet;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BACnet.class, name = "BACNET"),
})
public interface DeviceDataAcquisitionProperties {
    @ToString
    class BACnet implements DeviceDataAcquisitionProperties, CollectionDelegate<BacnetObjectProperty> {
        @Valid
        @JsonDeserialize(as = LinkedHashSet.class)
        private Set<BacnetObjectProperty> points;

        protected BACnet() {
        }

        @Override
        public Collection<BacnetObjectProperty> delegate() {
            return points;
        }

        protected Set<BacnetObjectProperty> getPoints() {
            return points;
        }
    }

    @ToString
    class Null implements DeviceDataAcquisitionProperties {
        public static final Null INSTANCE = new Null();

        private Null() {
        }
    }
}
