package cn.ecosync.ibms.system;

import cn.ecosync.ibms.system.command.PutDictionaryCommand;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class SystemConfig {
    @Bean
    public Module systemTypeModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.registerSubtypes(new NamedType(PutDictionaryCommand.class, "PUT_DICTIONARY"));
        return simpleModule;
    }
}
