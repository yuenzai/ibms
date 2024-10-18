package cn.ecosync.ibms.scheduling.task;

import cn.ecosync.ibms.bacnet.model.BacnetDeviceExtra;
import cn.ecosync.ibms.bacnet.query.GetBacnetDeviceStatusBatchQuery;
import cn.ecosync.ibms.device.event.DeviceStatusUpdatedEvent;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.query.SearchDeviceListQuery;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.scheduling.SchedulingTask;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ReadDeviceStatusBatchTaskBean implements SchedulingTask<ReadDeviceStatusBatchTaskBean.TaskParams> {
    private static final String SCHEDULING_TASK_TYPE = "ReadDeviceStatusBatch";

    @Bean
    public JobDetail readDeviceStatusBatchJobDetail() {
        return JobBuilder.newJob(ReadDeviceStatusBatchTaskBean.Task.class)
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

        @Override
        @Transactional
        public void execute(JobExecutionContext context) {
            List<DeviceDto> deviceDtoList = queryBus.execute(new SearchDeviceListQuery(true)).stream()
                    .filter(in -> in.getDeviceExtra() instanceof BacnetDeviceExtra && CollectionUtils.notEmpty(in.getDevicePoints()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(deviceDtoList)) {
                return;
            }
            GetBacnetDeviceStatusBatchQuery query = new GetBacnetDeviceStatusBatchQuery(deviceDtoList);
            queryBus.execute(query).stream()
                    .filter(Objects::nonNull)
                    .map(DeviceStatusUpdatedEvent::new)
                    .forEach(eventBus::publish);
        }
    }

    @Getter
    @ToString
    public static class TaskParams implements SchedulingTaskParams {
        @Override
        public String type() {
            return SCHEDULING_TASK_TYPE;
        }

        @Override
        public Map<String, Object> toParams() {
            return Collections.emptyMap();
        }
    }
}