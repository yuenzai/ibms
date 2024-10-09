package cn.ecosync.ibms.scheduling.task;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.bacnet.BacnetDeviceProperties;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Map;

@Configuration
@ConditionalOnClass(Scheduler.class)
public class ReadDeviceStatusTaskConfiguration {
    @Bean
    public JobDetail readDeviceStatus() {
        return JobBuilder.newJob(ReadDeviceStatusTask.class)
                .withIdentity(ReadDeviceStatusTask.SCHEDULING_TASK_TYPE)
                .storeDurably()
                .build();
    }

    @Bean
    public Module readDeviceStatusTaskTypeModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.registerSubtypes(new NamedType(ReadDeviceStatusTaskParams.class, ReadDeviceStatusTask.SCHEDULING_TASK_TYPE));
        return simpleModule;
    }

    /**
     * 如果程序包含 spring-boot-starter-quartz 依赖，可以不用继承 {@link QuartzJobBean} 类也能够获得依赖注入的能力<p>
     * 因为自动配置类（{@link QuartzAutoConfiguration}）使用了 {@link SpringBeanJobFactory} 作为 jobFactory，该类起到了和 {@link QuartzJobBean} 一样的作用
     */
    @Slf4j
    @RequiredArgsConstructor
    public static class ReadDeviceStatusTask implements Job {
        public static final String SCHEDULING_TASK_TYPE = "ReadDeviceStatus";

        private final DeviceRepository deviceRepository;
        private final BacnetService bacnetService;
        @Setter
        private String deviceCode;

        @Override
        @Transactional(readOnly = true)
        public void execute(JobExecutionContext context) {
            Assert.hasText(this.deviceCode, "Device code is required");
            DeviceId deviceId = new DeviceId(this.deviceCode);
            Device device = this.deviceRepository.get(deviceId).orElse(null);
            if (device == null) {
                log.info("Device not found: {}", deviceId);
                return;
            }
            if (device.getDeviceProperties() instanceof BacnetDeviceProperties) {
                this.bacnetService.readPropertiesToFile(device);
            }
        }
    }

    @Getter
    @ToString
    public static class ReadDeviceStatusTaskParams implements SchedulingTaskParams {
        @NotBlank
        private String deviceCode;

        @Override
        public String type() {
            return ReadDeviceStatusTask.SCHEDULING_TASK_TYPE;
        }

        @Override
        public Map<String, Object> toParams() {
            return Collections.singletonMap("deviceCode", this.deviceCode);
        }
    }
}
