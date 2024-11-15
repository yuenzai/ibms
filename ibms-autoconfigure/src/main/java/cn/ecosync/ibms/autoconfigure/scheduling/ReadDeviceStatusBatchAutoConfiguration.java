package cn.ecosync.ibms.autoconfigure.scheduling;

import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.orchestration.ReadDeviceStatusBatch;
import cn.ecosync.ibms.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import static cn.ecosync.ibms.orchestration.ReadDeviceStatusBatch.JOB_ID;

@AutoConfiguration(before = QuartzAutoConfiguration.class)
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class, PlatformTransactionManager.class})
public class ReadDeviceStatusBatchAutoConfiguration {
    @Bean
    public JobDetail readDeviceStatusBatchJobDetail() {
        return JobBuilder.newJob(ReadDeviceStatusBatchJob.class)
                .withIdentity(JOB_ID)
                .storeDurably()
                .build();
    }

    @RequiredArgsConstructor
    public static class ReadDeviceStatusBatchJob implements Job {
        private final QueryBus queryBus;
        private final EventBus eventBus;

        @Override
        @Transactional
        public void execute(JobExecutionContext context) {
            ReadDeviceStatusBatch.run(queryBus, eventBus);
        }
    }
}
