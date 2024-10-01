package cn.ecosync.ibms.bacnet.config;

import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
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
        simpleModule.registerSubtypes(new NamedType(BacnetReadPropertyMultipleQuery.class, "BACNET_READ_PROPERTY_MULTIPLE"));
        return simpleModule;
    }
}
