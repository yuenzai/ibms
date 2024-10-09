package cn.ecosync.ibms.scheduling.task;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointValue;
import cn.ecosync.ibms.device.model.bacnet.*;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import cn.ecosync.ibms.device.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public static class ReadDeviceStatusTask implements Job {
        public static final String SCHEDULING_TASK_TYPE = "ReadDeviceStatus";
        public static final String BACNET_SERVICE_URL = "BACNET_SERVICE_URL";

        private final DeviceRepository deviceRepository;
        private final RestTemplate restTemplate;
        private final String bacnetServiceUrl;
        @Setter
        private String deviceCode;

        public ReadDeviceStatusTask(DeviceRepository deviceRepository, RestTemplateBuilder builder, Environment environment) {
            this.deviceRepository = deviceRepository;
            this.restTemplate = builder.setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                    .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
                    .build();
            this.bacnetServiceUrl = environment.getProperty(BACNET_SERVICE_URL);
        }

        @Override
        @Transactional(readOnly = true)
        public void execute(JobExecutionContext context) {
            Assert.hasText(this.deviceCode, "Device code is required");
            DeviceId deviceId = new DeviceId(this.deviceCode);
            Device device = deviceRepository.get(deviceId).orElse(null);
            if (device == null) {
                log.info("Device not found: {}", deviceId);
                return;
            }
            Map<DevicePoint, DevicePointValue> deviceStatus = null;
            if (device.getDeviceProperties() instanceof BacnetDeviceProperties) {
                deviceStatus = readBacnetDeviceStatus(device);
            }
            if (CollectionUtils.notEmpty(deviceStatus)) {
                log.info("device status: {}", deviceStatus);
            }
        }

        private Map<DevicePoint, DevicePointValue> readBacnetDeviceStatus(Device device) {
            if (this.bacnetServiceUrl == null) {
                log.info("Bacnet service url missing");
                return Collections.emptyMap();
            }
            // 设备配置属性
            BacnetDeviceProperties deviceProperties = (BacnetDeviceProperties) device.getDeviceProperties();
            // 点位配置属性
            List<DevicePoint> devicePoints = device.getDevicePoints().values().stream()
                    .filter(in -> in.getPointProperties() instanceof BacnetObjectProperty)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(devicePoints)) {
                log.info("Device points not found: {}", this.deviceCode);
                return Collections.emptyMap();
            }

            Map<BacnetObject, List<BacnetProperty>> objectProperties = devicePoints.stream()
                    .map(in -> (BacnetObjectProperty) in.getPointProperties())
                    .collect(Collectors.groupingBy(BacnetObjectProperty::toBacnetObject, Collectors.mapping(BacnetObjectProperty::toBacnetProperty, Collectors.toList())));

            BacnetReadPropertyMultipleQuery query = new BacnetReadPropertyMultipleQuery(deviceProperties.getDeviceInstance(), objectProperties);
            List<ReadPropertyMultipleAck> ack = restTemplate.exchange(bacnetServiceUrl + "/bacnet/readpropm", HttpMethod.POST, new HttpEntity<>(query), new ParameterizedTypeReference<List<ReadPropertyMultipleAck>>() {
            }).getBody();
            Map<BacnetObjectProperty, BacnetPropertyValue> simpleMap = ReadPropertyMultipleAck.toSimpleMap(ack);
            return devicePoints.stream()
                    .collect(Collectors.toMap(Function.identity(), in -> simpleMap.get((BacnetObjectProperty) in.getPointProperties())));// 注意这里没有对 key 去重，如果重复会报错
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
