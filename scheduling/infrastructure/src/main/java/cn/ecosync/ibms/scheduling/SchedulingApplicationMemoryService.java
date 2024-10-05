package cn.ecosync.ibms.scheduling;

import cn.ecosync.ibms.scheduling.model.Scheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingTask;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingApplicationMemoryService implements SchedulingApplicationService, ApplicationRunner, DisposableBean {
    private final TaskScheduler taskScheduler;
    private final SchedulingRepository schedulingRepository;
    private final Map<SchedulingId, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();

    @Override
    public void execute(SchedulingTask schedulingTask) {
        schedulingTask.run();
    }

    @Async
    @Override
    public void executeAsync(SchedulingTask schedulingTask) {
        schedulingTask.run();
    }

    @Override
    public void schedule(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, SchedulingTask schedulingTask) {
        Trigger trigger;
        if (schedulingTrigger instanceof SchedulingTrigger.Cron) {
            SchedulingTrigger.Cron cron = (SchedulingTrigger.Cron) schedulingTrigger;
            trigger = new CronTrigger(cron.getExpression());
        } else {
            return;
        }
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(schedulingTask, trigger);
        scheduledFutureMap.put(schedulingId, scheduledFuture);
    }

    @Override
    public void cancel(SchedulingId schedulingId) {
        // 不要移除 future 引用，防止任务意外没有停止，允许重复执行停止任务的操作
        get(schedulingId)
                .ifPresent(in -> in.cancel(false));
    }

    @Override
    public Boolean isRunning(SchedulingId schedulingId) {
        ScheduledFuture<?> scheduledFuture = get(schedulingId).orElse(null);
        if (scheduledFuture == null) {
            return null;
        }
        return !scheduledFuture.isDone();
    }

    @Async
    @Override
    @Transactional(readOnly = true)
    public void run(ApplicationArguments args) {
        for (Scheduling scheduling : schedulingRepository.list(true)) {
            schedule(scheduling.getSchedulingId(), scheduling.getSchedulingTrigger(), scheduling.getSchedulingTask());
        }
    }

    @Override
    public void destroy() {
        scheduledFutureMap.keySet().stream()
                .peek(in -> log.info("Cancel scheduling task: {}", in))
                .forEach(this::cancel);
    }

    private Optional<ScheduledFuture<?>> get(SchedulingId schedulingId) {
        return Optional.ofNullable(scheduledFutureMap.get(schedulingId));
    }
}
