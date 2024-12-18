//package cn.ecosync.ibms.autoconfigure.scheduling;
//
//import cn.ecosync.ibms.orchestration.ReadDeviceStatus;
//import cn.ecosync.iframework.event.EventBus;
//import cn.ecosync.iframework.query.QueryBus;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import org.quartz.*;
//import org.springframework.boot.autoconfigure.AutoConfiguration;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//
//import static cn.ecosync.ibms.orchestration.ReadDeviceStatus.JOB_ID;
//
//@AutoConfiguration(before = QuartzAutoConfiguration.class)
//@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class, PlatformTransactionManager.class})
//public class ReadDeviceStatusAutoConfiguration {
//    @Bean
//    public JobDetail readDeviceStatusJobDetail() {
//        return JobBuilder.newJob(ReadDeviceStatusJob.class)
//                .withIdentity(JOB_ID)
//                .storeDurably()
//                .build();
//    }
//
//    @RequiredArgsConstructor
//    public static class ReadDeviceStatusJob implements Job {
//        private final QueryBus queryBus;
//        private final EventBus eventBus;
//        @Setter
//        private String deviceCode;
//
//        @Override
//        @Transactional
//        public void execute(JobExecutionContext context) {
//            Assert.hasText(deviceCode, "deviceCode is required");
//            ReadDeviceStatus.run(queryBus, eventBus, deviceCode);
//        }
//    }
//}
