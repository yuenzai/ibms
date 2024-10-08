package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.DeviceConstant;
import cn.ecosync.ibms.device.model.bacnet.BacnetDeviceProperties;
import cn.ecosync.ibms.device.model.bacnet.BacnetNetworkProperties;
import cn.ecosync.ibms.device.model.bacnet.BacnetObjectProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString
public enum DeviceTypeEnum {
    DEVICE_TYPE_BACNET(DeviceConstant.BACNET, BacnetNetworkProperties.class, BacnetDeviceProperties.class, BacnetObjectProperty.class),
    ;

    private final String type;
    private final Class<?> networkType;
    private final Class<?> devicePropertiesType;
    private final Class<?> devicePointPropertiesType;

    DeviceTypeEnum(String type, Class<?> networkType, Class<?> devicePropertiesType, Class<?> devicePointPropertiesType) {
        this.type = type;
        this.networkType = networkType;
        this.devicePropertiesType = devicePropertiesType;
        this.devicePointPropertiesType = devicePointPropertiesType;
    }

    private static final Map<String, DeviceTypeEnum> ENUM_MAP;

    static {
        ENUM_MAP = Arrays.stream(values())
                .collect(Collectors.toMap(DeviceTypeEnum::getType, Function.identity()));
    }

    public static DeviceTypeEnum of(String type) {
        return ENUM_MAP.get(type);
    }
}
