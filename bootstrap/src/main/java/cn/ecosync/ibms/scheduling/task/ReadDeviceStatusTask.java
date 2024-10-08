package cn.ecosync.ibms.scheduling.task;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointValue;
import cn.ecosync.ibms.device.model.bacnet.*;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import cn.ecosync.ibms.device.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ReadDeviceStatusTask extends QuartzJobBean {
    public static final String BACNET_SERVICE_URL = "BACNET_SERVICE_URL";

    @Setter
    private DeviceRepository deviceRepository;
    private RestTemplate restTemplate;
    private String bacnetServiceUrl;
    @Setter
    private DeviceId deviceId;

    public void setRestTemplate(RestTemplateBuilder builder) {
        this.restTemplate = builder.setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
    }

    public void setBacnetServiceUrl(Environment environment) {
        this.bacnetServiceUrl = environment.getProperty(BACNET_SERVICE_URL);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (this.deviceId == null) {
            log.info("DeviceId missing");
            return;
        }
        Device device = deviceRepository.get(this.deviceId).orElse(null);
        if (device == null) {
            log.info("Device not found: {}", this.deviceId);
            return;
        }
        Map<DevicePoint, DevicePointValue> deviceStatus = null;
        if (device.getDeviceProperties() instanceof BacnetDeviceProperties) {
            deviceStatus = readBacnetDeviceStatus(device);
        }
        log.info("device status: {}", deviceStatus);
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
