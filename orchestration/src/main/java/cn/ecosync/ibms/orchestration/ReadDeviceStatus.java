package cn.ecosync.ibms.orchestration;

import cn.ecosync.ibms.bacnet.BacnetMapper;
import cn.ecosync.ibms.bacnet.model.BacnetDeviceExtra;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple;
import cn.ecosync.ibms.device.event.DeviceStatusUpdatedEvent;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ReadDeviceStatus {
    public static final String JOB_ID = "ReadDeviceStatus";

    public static void run(QueryBus queryBus, EventBus eventBus, String deviceCode) {
        DeviceDto device = queryBus.execute(new GetDeviceQuery(deviceCode, true));
        if (device == null) {
            log.info("Device not found: {}", deviceCode);
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

    @Getter
    @ToString
    public static class TaskParams implements SchedulingTaskParams {
        @NotBlank
        private String deviceCode;

        @Override
        public String type() {
            return JOB_ID;
        }

        @Override
        public Map<String, Object> toParams() {
            return Collections.singletonMap("deviceCode", deviceCode);
        }
    }
}
