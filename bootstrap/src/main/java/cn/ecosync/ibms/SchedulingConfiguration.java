package cn.ecosync.ibms;

import cn.ecosync.ibms.scheduling.task.ReadDeviceStatusTask;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(JobDetail.class)
public class SchedulingConfiguration {
    @Bean
    public JobDetail readDeviceStatus() {
        return JobBuilder.newJob(ReadDeviceStatusTask.class)
                .withIdentity("readDeviceStatus")
                .storeDurably()
                .build();
    }
}
