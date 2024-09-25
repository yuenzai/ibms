package cn.ecosync.ibms.device;

import cn.ecosync.ibms.device.command.*;
import cn.ecosync.ibms.device.model.DeviceConstant;
import cn.ecosync.ibms.device.model.bacnet.BacnetDevicePointProperties;
import cn.ecosync.ibms.device.model.bacnet.BacnetDeviceProperties;
import cn.ecosync.ibms.device.model.bacnet.BacnetNetworkProperties;
import cn.ecosync.ibms.device.query.BacnetReadPropertyMultipleQuery;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class DeviceConfig {
    @Bean
    public Module deviceTypeModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.registerSubtypes(new NamedType(BacnetNetworkProperties.class, DeviceConstant.BACNET));
        simpleModule.registerSubtypes(new NamedType(BacnetDeviceProperties.class, DeviceConstant.BACNET));
        simpleModule.registerSubtypes(new NamedType(BacnetDevicePointProperties.class, DeviceConstant.BACNET));
        // command
        simpleModule.registerSubtypes(new NamedType(AddDeviceCommand.class, "ADD_DEVICE"));
        simpleModule.registerSubtypes(new NamedType(UpdateDeviceCommand.class, "UPDATE_DEVICE"));
        simpleModule.registerSubtypes(new NamedType(RemoveDeviceCommand.class, "REMOVE_DEVICE"));
        simpleModule.registerSubtypes(new NamedType(PutDevicePointCommand.class, "PUT_DEVICE_POINT"));
        simpleModule.registerSubtypes(new NamedType(RemoveDevicePointCommand.class, "REMOVE_DEVICE_POINT"));
        // query
        simpleModule.registerSubtypes(new NamedType(BacnetReadPropertyMultipleQuery.class, "BACNET_READ_PROPERTY_MULTIPLE"));
        return simpleModule;
    }
}
