package cn.ecosync.ibms.scheduling.task;

import cn.ecosync.ibms.bacnet.model.BacnetDeviceExtra;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleToFileQuery;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.scheduling.SchedulingTask;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Map;

@Component
public class ReadDeviceStatusTaskBean implements SchedulingTask<ReadDeviceStatusTaskBean.ReadDeviceStatusTaskParams> {
    private static final String SCHEDULING_TASK_TYPE = "ReadDeviceStatus";

    @Bean
    public JobDetail readDeviceStatus() {
        return JobBuilder.newJob(ReadDeviceStatusTaskBean.ReadDeviceStatusTask.class)
                .withIdentity(SCHEDULING_TASK_TYPE)
                .storeDurably()
                .build();
    }

    @Override
    public String taskId() {
        return SCHEDULING_TASK_TYPE;
    }

    @Override
    public Class<ReadDeviceStatusTaskParams> taskParamsType() {
        return ReadDeviceStatusTaskParams.class;
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class ReadDeviceStatusTask implements Job {
        private final QueryBus queryBus;
        @Setter
        private String deviceCode;

        @Override
        @Transactional(readOnly = true)
        public void execute(JobExecutionContext context) {
            Assert.hasText(this.deviceCode, "deviceCode is required");
            DeviceDto device = queryBus.execute(new GetDeviceQuery(this.deviceCode, true));
            if (device == null) {
                log.info("Device not found: {}", this.deviceCode);
                return;
            }
            if (device.getDeviceExtra() instanceof BacnetDeviceExtra) {
                BacnetReadPropertyMultipleToFileQuery.newInstance(device)
                        .ifPresent(queryBus::execute);
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
            return SCHEDULING_TASK_TYPE;
        }

        @Override
        public Map<String, Object> toParams() {
            return Collections.singletonMap("deviceCode", this.deviceCode);
        }
    }
}
