package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.device.model.DevicePointProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
public class BacnetDevicePointProperties implements DevicePointProperties {
    @NotNull(message = "objectType must not be null")
    private Integer objectType;
    @NotNull(message = "objectId must not be null")
    private Integer objectId;
    @NotNull(message = "propertyId must not be null")
    private Integer propertyId;

    protected BacnetDevicePointProperties() {
    }

    public BacnetDevicePointProperties(Integer objectType, Integer objectId, Integer propertyId) {
        Assert.notNull(objectType, "bacnet object type must not be null");
        Assert.notNull(objectId, "bacnet object id must not be null");
        Assert.notNull(propertyId, "bacnet property id must not be null");
        this.objectType = objectType;
        this.objectId = objectId;
        this.propertyId = propertyId;
    }
}
