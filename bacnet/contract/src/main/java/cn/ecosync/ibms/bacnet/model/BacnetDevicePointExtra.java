package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.DevicePointExtra;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class BacnetDevicePointExtra implements DevicePointExtra {
    @NotNull
    private BacnetObjectType objectType;
    @NotNull
    private Integer objectInstance;
    @NotNull
    private BacnetPropertyId propertyIdentifier;
    private Integer propertyArrayIndex;

    public BacnetObject toBacnetObject() {
        return new BacnetObject(this.objectType, this.objectInstance);
    }

    public BacnetProperty toBacnetProperty() {
        return new BacnetProperty(this.propertyIdentifier, this.propertyArrayIndex);
    }
}
