package cn.ecosync.ibms.device.model.bacnet;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum BacnetPropertyValueType {
    BACNET_APPLICATION_TAG_NULL(0),
    BACNET_APPLICATION_TAG_BOOLEAN(1),
    BACNET_APPLICATION_TAG_UNSIGNED_INT(2),
    BACNET_APPLICATION_TAG_SIGNED_INT(3),
    BACNET_APPLICATION_TAG_REAL(4),
    BACNET_APPLICATION_TAG_DOUBLE(5),
    BACNET_APPLICATION_TAG_OCTET_STRING(6),
    BACNET_APPLICATION_TAG_CHARACTER_STRING(7),
    BACNET_APPLICATION_TAG_BIT_STRING(8),
    BACNET_APPLICATION_TAG_ENUMERATED(9),
    BACNET_APPLICATION_TAG_DATE(10),
    BACNET_APPLICATION_TAG_TIME(11),
    BACNET_APPLICATION_TAG_OBJECT_ID(12),
    ;

    private final int tag;

    BacnetPropertyValueType(int tag) {
        this.tag = tag;
    }
}