//package cn.ecosync.ibms.device.model.bacnet;
//
//import cn.ecosync.ibms.device.model.DevicePointProperties;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.ToString;
//
//import javax.validation.constraints.NotNull;
//
//@Getter
//@ToString
//@EqualsAndHashCode
//public class BacnetDevicePointProperties implements DevicePointProperties {
//    @NotNull(message = "objectType must not be null")
//    private BACnetObjectType objectType;
//    @NotNull(message = "objectInstance must not be null")
//    private Integer objectInstance;
//    @NotNull(message = "propertyId must not be null")
//    private BacnetPropertyId propertyId;
//    private Integer propertyArrayIndex;
//
//    public BacnetObject toBacnetObject() {
//        return new BacnetObject(objectType, objectInstance);
//    }
//
//    public BacnetProperty toBacnetProperty() {
//        return new BacnetProperty(propertyId, propertyArrayIndex);
//    }
//}
