package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.dto.BacnetObjectPropertiesWithName;
import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

import static cn.ecosync.ibms.device.model.DeviceDataAcquisitionProperties.BACnet;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BACnet.class, name = BACnet.TYPE),
})
public interface DeviceDataAcquisitionProperties {
    String getType();

    @ToString
    class BACnet implements DeviceDataAcquisitionProperties {
        public static final String TYPE = "BACNET";

        @Valid
        private List<BacnetObjectPropertiesWithName> fields;

        protected BACnet() {
        }

        public BACnet(List<BacnetObjectPropertiesWithName> fields) {
            Assert.isTrue(CollectionUtils.hasUniqueElement(fields, BacnetObjectPropertiesWithName::getName), "fields must contain unique name");
            Assert.isTrue(CollectionUtils.hasUniqueElement(fields), "fields must contain unique element");
            this.fields = fields;
        }

        public Collection<BacnetObjectPropertiesWithName> getFields() {
            return CollectionUtils.nullSafeOf(fields);
        }

        @Override
        public String getType() {
            return TYPE;
        }

        @JsonIgnore
        @AssertTrue(message = "fields must contain unique name")
        public boolean isUniqueName() {
            return CollectionUtils.hasUniqueElement(fields, BacnetObjectPropertiesWithName::getName);
        }

        @JsonIgnore
        @AssertTrue(message = "fields must contain unique element")
        public boolean isUniqueElement() {
            return CollectionUtils.hasUniqueElement(fields);
        }
    }

    @ToString
    class Null implements DeviceDataAcquisitionProperties {
        public static final Null INSTANCE = new Null();

        private Null() {
        }

        @Override
        public String getType() {
            return "";
        }
    }
}
