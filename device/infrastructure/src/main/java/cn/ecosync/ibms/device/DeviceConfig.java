package cn.ecosync.ibms.device;

import cn.ecosync.ibms.device.model.bacnet.BacnetDevicePointProperties;
import cn.ecosync.ibms.device.model.bacnet.BacnetDeviceProperties;
import cn.ecosync.ibms.device.model.bacnet.BacnetNetworkProperties;
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
        simpleModule.registerSubtypes(new NamedType(BacnetDeviceProperties.class, "bacnet"));
        simpleModule.registerSubtypes(new NamedType(BacnetDevicePointProperties.class, "bacnet"));
        simpleModule.registerSubtypes(new NamedType(BacnetNetworkProperties.class, "bacnet"));
        return simpleModule;
    }
}
