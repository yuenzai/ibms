package cn.ecosync.ibms.device;

import cn.ecosync.ibms.bacnet.model.BacnetDeviceExtra;
import cn.ecosync.ibms.bacnet.model.BacnetDevicePointExtra;
import cn.ecosync.ibms.device.command.*;
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
        simpleModule.registerSubtypes(new NamedType(AddDeviceCommand.class, "ADD_DEVICE"));
        simpleModule.registerSubtypes(new NamedType(UpdateDeviceCommand.class, "UPDATE_DEVICE"));
        simpleModule.registerSubtypes(new NamedType(RemoveDeviceCommand.class, "REMOVE_DEVICE"));
        simpleModule.registerSubtypes(new NamedType(PutDevicePointCommand.class, "PUT_DEVICE_POINT"));
        simpleModule.registerSubtypes(new NamedType(RemoveDevicePointCommand.class, "REMOVE_DEVICE_POINT"));

        simpleModule.registerSubtypes(new NamedType(BacnetDeviceExtra.class, "BACNET"));
        simpleModule.registerSubtypes(new NamedType(BacnetDevicePointExtra.class, "BACNET"));
        return simpleModule;
    }
}
