package cn.ecosync.ibms.autoconfigure.serde;

import cn.ecosync.ibms.bacnet.model.BacnetDeviceExtra;
import cn.ecosync.ibms.bacnet.model.BacnetDevicePointExtra;
import cn.ecosync.ibms.orchestration.ReadDeviceStatus;
import cn.ecosync.ibms.orchestration.ReadDeviceStatusBatch;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(before = org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class)
@ConditionalOnClass(ObjectMapper.class)
public class JacksonAutoConfiguration {
    @Bean
    public Module jsonTypeModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.registerSubtypes(new NamedType(ReadDeviceStatusBatch.TaskParams.class, ReadDeviceStatusBatch.JOB_ID));
        simpleModule.registerSubtypes(new NamedType(ReadDeviceStatus.TaskParams.class, ReadDeviceStatus.JOB_ID));
        simpleModule.registerSubtypes(new NamedType(BacnetDeviceExtra.class, "BACNET"));
        simpleModule.registerSubtypes(new NamedType(BacnetDevicePointExtra.class, "BACNET"));
        return simpleModule;
    }
}
