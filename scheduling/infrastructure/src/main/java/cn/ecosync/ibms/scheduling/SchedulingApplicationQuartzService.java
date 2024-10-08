package cn.ecosync.ibms.scheduling;

import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingState;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.Key;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.scheduling.SchedulingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnClass(Scheduler.class)
public class SchedulingApplicationQuartzService implements SchedulingApplicationService {
    private final Scheduler scheduler;

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
    public void execute(String schedulingTask) {
        try {
            JobKey jobKey = JobKey.jobKey(schedulingTask);
            existsBy(schedulingTask);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void schedule(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, String schedulingTask, Map<String, Object> schedulingTaskParams) {
        try {
            JobKey jobKey = JobKey.jobKey(schedulingTask);
            existsBy(schedulingTask);
            TriggerKey triggerKey = toTriggerKey(schedulingId);
            Trigger trigger;
            if (schedulingTrigger instanceof SchedulingTrigger.Cron) {
                SchedulingTrigger.Cron cron = (SchedulingTrigger.Cron) schedulingTrigger;
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron.getExpression()).withMisfireHandlingInstructionDoNothing())
                        .forJob(jobKey)
                        .usingJobData(new JobDataMap(schedulingTaskParams))
                        .build();
            } else {
                return;
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
    public void existsBy(String schedulingTask) {
        try {
            JobKey jobKey = JobKey.jobKey(schedulingTask);
            if (!scheduler.checkExists(jobKey)) {
                throw new IllegalArgumentException("Illegal scheduling task: " + schedulingTask);
            }
        } catch (SchedulerException e) {
            throw new SchedulingException(e.getMessage(), e.getCause());
        }
    }

    private TriggerKey toTriggerKey(SchedulingId schedulingId) {
        return TriggerKey.triggerKey(schedulingId.getSchedulingName());
    }
}
