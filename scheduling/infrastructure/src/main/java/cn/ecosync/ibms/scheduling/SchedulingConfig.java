package cn.ecosync.ibms.scheduling;

import cn.ecosync.ibms.scheduling.command.AddSchedulingCommand;
import cn.ecosync.ibms.scheduling.command.EnableOrDisableSchedulingCommand;
import cn.ecosync.ibms.scheduling.command.ResetSchedulingCommand;
import cn.ecosync.ibms.scheduling.command.UpdateSchedulingCommand;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class SchedulingConfig {
    @Bean
    public Module schedulingTypeModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.registerSubtypes(new NamedType(AddSchedulingCommand.class, "ADD_SCHEDULING"));
        simpleModule.registerSubtypes(new NamedType(UpdateSchedulingCommand.class, "UPDATE_SCHEDULING"));
        simpleModule.registerSubtypes(new NamedType(EnableOrDisableSchedulingCommand.class, "ENABLE_OR_DISABLE_SCHEDULING"));
        simpleModule.registerSubtypes(new NamedType(ResetSchedulingCommand.class, "RESET_SCHEDULING"));
        return simpleModule;
    }
}
