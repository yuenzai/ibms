package cn.ecosync.ibms.scheduling.task;

import cn.ecosync.ibms.bacnet.BacnetMapper;
import cn.ecosync.ibms.bacnet.model.BacnetDeviceExtra;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple;
import cn.ecosync.ibms.device.event.DeviceStatusUpdatedEvent;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.scheduling.SchedulingTask;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import cn.ecosync.ibms.util.CollectionUtils;
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
import java.util.Optional;

@Component
public class ReadDeviceStatusTaskBean implements SchedulingTask<ReadDeviceStatusTaskBean.TaskParams> {
    private static final String SCHEDULING_TASK_TYPE = "ReadDeviceStatus";

    @Bean
    public JobDetail readDeviceStatusJobDetail() {
        return JobBuilder.newJob(Task.class)
                .withIdentity(SCHEDULING_TASK_TYPE)
                .storeDurably()
                .build();
    }

    @Override
    public String taskId() {
        return SCHEDULING_TASK_TYPE;
    }

    @Override
    public Class<TaskParams> taskParamsType() {
        return TaskParams.class;
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class Task implements Job {
        private final QueryBus queryBus;
        private final EventBus eventBus;
        @Setter
        private String deviceCode;

        @Override
        @Transactional
        public void execute(JobExecutionContext context) {
            Assert.hasText(this.deviceCode, "deviceCode is required");
            DeviceDto device = queryBus.execute(new GetDeviceQuery(this.deviceCode, true));
            if (device == null) {
                log.info("Device not found: {}", this.deviceCode);
                return;
            }
            if (device.getDeviceExtra() instanceof BacnetDeviceExtra && CollectionUtils.notEmpty(device.getDevicePoints())) {
                BacnetReadPropertyMultiple readPropertyMultipleService = BacnetMapper.toReadPropertyMultipleService(device);
                BacnetReadPropertyMultipleQuery query = new BacnetReadPropertyMultipleQuery(readPropertyMultipleService);
                Optional.ofNullable(queryBus.execute(query))
                        .map(ack -> BacnetMapper.toDeviceStatus(device, ack))
                        .map(DeviceStatusUpdatedEvent::new)
                        .ifPresent(eventBus::publish);
            }
        }
    }

    @Getter
    @ToString
    public static class TaskParams implements SchedulingTaskParams {
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
