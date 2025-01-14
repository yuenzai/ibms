package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.dto.DeviceSchema;
import cn.ecosync.iframework.util.CollectionUtils;

import java.util.Collection;

public interface IDeviceSchemas {
    Collection<? extends DeviceSchema> getSchemas();

    default boolean checkUniqueName() {
        return CollectionUtils.hasUniqueElement(getSchemas(), DeviceSchema::getName);
    }
}
