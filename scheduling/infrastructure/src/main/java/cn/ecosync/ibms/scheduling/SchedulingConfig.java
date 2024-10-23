//package cn.ecosync.ibms.scheduling;
//
//import cn.ecosync.ibms.scheduling.command.*;
//import com.fasterxml.jackson.databind.Module;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.NamedType;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConditionalOnClass(ObjectMapper.class)
//public class SchedulingConfig {
//    @Bean
//    public Module schedulingTypeModule() {
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.registerSubtypes(new NamedType(AddSchedulingCommand.class, "ADD_SCHEDULING"));
//        simpleModule.registerSubtypes(new NamedType(UpdateSchedulingCommand.class, "UPDATE_SCHEDULING"));
//        simpleModule.registerSubtypes(new NamedType(RemoveSchedulingCommand.class, "REMOVE_SCHEDULING"));
//        simpleModule.registerSubtypes(new NamedType(SwitchSchedulingCommand.class, "SWITCH_SCHEDULING"));
//        simpleModule.registerSubtypes(new NamedType(ResetSchedulingCommand.class, "RESET_SCHEDULING"));
//        return simpleModule;
//    }
//}
