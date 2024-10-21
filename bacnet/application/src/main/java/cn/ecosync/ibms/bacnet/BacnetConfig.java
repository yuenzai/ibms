package cn.ecosync.ibms.bacnet;

import cn.ecosync.ibms.bacnet.model.BacnetDeviceExtra;
import cn.ecosync.ibms.bacnet.model.BacnetDevicePointExtra;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class BacnetConfig {
    @Bean
    public Module bacnetTypeModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.registerSubtypes(new NamedType(BacnetDeviceExtra.class, "BACNET"));
        simpleModule.registerSubtypes(new NamedType(BacnetDevicePointExtra.class, "BACNET"));
        return simpleModule;
    }
}
