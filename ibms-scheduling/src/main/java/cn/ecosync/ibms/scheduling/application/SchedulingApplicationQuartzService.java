package cn.ecosync.ibms.scheduling.application;

import cn.ecosync.ibms.domain.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.domain.scheduling.SchedulingId;
import cn.ecosync.ibms.scheduling.dto.SchedulingState;
import cn.ecosync.ibms.scheduling.dto.SchedulingTaskParams;
import cn.ecosync.ibms.scheduling.dto.SchedulingTrigger;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.serde.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.OperableTrigger;
import org.quartz.utils.Key;
import org.springframework.scheduling.SchedulingException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingApplicationQuartzService implements SchedulingApplicationService {
    private final Scheduler scheduler;
    private final JsonSerde jsonSerde;

    @Override
    public List<String> getSchedulingTasks() {
        try {
            return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JobKey.DEFAULT_GROUP)).stream()
                    .map(Key::getName)
                    .collect(Collectors.toList());
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void execute(SchedulingTaskParams schedulingTaskParams) {
        try {
            JobKey jobKey = JobKey.jobKey(schedulingTaskParams.type());
            checkExists(schedulingTaskParams);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void schedule(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams) {
        try {
            JobKey jobKey = JobKey.jobKey(schedulingTaskParams.type());
            checkExists(schedulingTaskParams);
            TriggerKey triggerKey = toTriggerKey(schedulingId);
            Trigger trigger;
            if (schedulingTrigger instanceof SchedulingTrigger.Cron) {
                SchedulingTrigger.Cron cron = (SchedulingTrigger.Cron) schedulingTrigger;
                Map<String, Object> params = jsonSerde.convert(schedulingTaskParams, new TypeReference<Map<String, Object>>() {
                });
                JobDataMap jobDataMap = Optional.ofNullable(params)
                        .map(JobDataMap::new)
                        .orElseGet(JobDataMap::new);
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron.getExpression()).withMisfireHandlingInstructionDoNothing())
                        .forJob(jobKey)
                        .usingJobData(jobDataMap)
                        .build();
            } else {
                throw new IllegalArgumentException("unsupported scheduling trigger type: " + schedulingTrigger.getClass().getName());
            }
            if (scheduler.checkExists(triggerKey)) {
                Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
                if (triggerState == Trigger.TriggerState.PAUSED) {
                    scheduler.resumeTrigger(triggerKey);
                    log.info("trigger resumed: {}", triggerKey);
                } else {
                    scheduler.rescheduleJob(triggerKey, trigger);
                    log.info("trigger rescheduled: {}", triggerKey);
                }
            } else {
                scheduler.scheduleJob(trigger);
                log.info("trigger scheduled: {}", triggerKey);
            }
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void cancel(SchedulingId schedulingId) {
        try {
            TriggerKey triggerKey = toTriggerKey(schedulingId);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.unscheduleJob(triggerKey);
                log.info("trigger unscheduled: {}", triggerKey);
            }
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void pause(SchedulingId schedulingId) {
        try {
            TriggerKey triggerKey = toTriggerKey(schedulingId);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
                log.info("trigger paused: {}", triggerKey);
            }
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void resetError(SchedulingId schedulingId) {
        try {
            TriggerKey triggerKey = toTriggerKey(schedulingId);
            if (scheduler.checkExists(triggerKey) && scheduler.getTriggerState(triggerKey) == Trigger.TriggerState.ERROR) {
                scheduler.resetTriggerFromErrorState(triggerKey);
                log.info("trigger has been reset: {}", triggerKey);
            }
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public SchedulingState getSchedulingState(SchedulingId schedulingId) {
        try {
            TriggerKey triggerKey = toTriggerKey(schedulingId);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            return SchedulingState.valueOf(triggerState.name());
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void checkExists(SchedulingTaskParams schedulingTaskParams) {
        try {
            JobKey jobKey = JobKey.jobKey(schedulingTaskParams.type());
            if (!scheduler.checkExists(jobKey)) {
                throw new IllegalArgumentException("Illegal scheduling task: " + schedulingTaskParams);
            }
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Date> computeNextFireTimes(SchedulingId schedulingId, int maxCount) {
        if (maxCount < 1) {
            throw new IllegalArgumentException("maxCount must be greater than 0");
        }
        try {
            TriggerKey triggerKey = toTriggerKey(schedulingId);
            return Optional.ofNullable(scheduler.getTrigger(triggerKey))
                    .filter(OperableTrigger.class::isInstance)
                    .map(in -> TriggerUtils.computeFireTimes((OperableTrigger) in, null, maxCount))
                    .orElse(Collections.emptyList());
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<Date> getPreviousFireTime(SchedulingId schedulingId) {
        try {
            TriggerKey triggerKey = toTriggerKey(schedulingId);
            return Optional.ofNullable(scheduler.getTrigger(triggerKey))
                    .map(Trigger::getPreviousFireTime);
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    private TriggerKey toTriggerKey(SchedulingId schedulingId) {
        return TriggerKey.triggerKey(schedulingId.getSchedulingName());
    }
}
