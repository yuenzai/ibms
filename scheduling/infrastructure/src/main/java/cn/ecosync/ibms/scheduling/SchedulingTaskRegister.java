package cn.ecosync.ibms.scheduling;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SchedulingTaskRegister {
    private final List<SchedulingTask<?>> schedulingTasks;

    @Bean
    public Module schedulingTaskParamsTypeModule() {
        SimpleModule simpleModule = new SimpleModule();
        for (SchedulingTask<?> schedulingTask : this.schedulingTasks) {
            simpleModule.registerSubtypes(new NamedType(schedulingTask.taskParamsType(), schedulingTask.taskId()));
        }
        return simpleModule;
    }
}
