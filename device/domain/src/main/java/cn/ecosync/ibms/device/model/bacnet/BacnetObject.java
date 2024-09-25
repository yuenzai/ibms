package cn.ecosync.ibms.device.model.bacnet;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetObject {
    private BACnetObjectType objectType;
    private Integer objectInstance;
}
