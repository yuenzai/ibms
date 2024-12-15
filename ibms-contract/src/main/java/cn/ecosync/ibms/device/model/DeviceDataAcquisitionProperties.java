package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.dto.BacnetObjectPropertyWithKey;
import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.ToString;
import org.springframework.util.Assert;

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
        @NotEmpty
        private List<BacnetObjectPropertyWithKey> bacnetPoints;

        protected BACnet() {
        }

        public BACnet(List<BacnetObjectPropertyWithKey> bacnetPoints) {
            Assert.notEmpty(bacnetPoints, "bacnetPoints must not be empty");
            Assert.isTrue(CollectionUtils.hasUniqueElement(bacnetPoints, BacnetObjectPropertyWithKey::getKey), "bacnetPoints must contain unique key");
            Assert.isTrue(CollectionUtils.hasUniqueElement(bacnetPoints, BacnetObjectPropertyWithKey::getBop), "bacnetPoints must contain unique point");
            this.bacnetPoints = bacnetPoints;
        }

        public List<BacnetObjectPropertyWithKey> getBacnetPoints() {
            return CollectionUtils.nullSafeOf(bacnetPoints);
        }

        @Override
        public String getType() {
            return TYPE;
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
