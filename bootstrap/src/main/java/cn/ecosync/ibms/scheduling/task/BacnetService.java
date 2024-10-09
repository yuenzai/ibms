package cn.ecosync.ibms.scheduling.task;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointValue;
import cn.ecosync.ibms.device.model.bacnet.*;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import cn.ecosync.ibms.device.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BacnetService {
    public static final String ENV_BACNET_SERVICE_URL = "BACNET_SERVICE_URL";
    private final RestTemplate restTemplate;
    private final String bacnetServiceUrl;

    public BacnetService(RestTemplateBuilder builder, Environment environment) {
        this.restTemplate = builder.setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        this.bacnetServiceUrl = environment.getProperty(ENV_BACNET_SERVICE_URL);
        Assert.hasText(this.bacnetServiceUrl, "Environment variable " + ENV_BACNET_SERVICE_URL + " is required");
    }

    public Map<DevicePoint, DevicePointValue> readProperties(Device device, RestTemplate restTemplate, String bacnetServiceUrl) {
        if (!StringUtils.hasText(bacnetServiceUrl)) {
            return Collections.emptyMap();
        }
        // 设备配置属性
        BacnetDeviceProperties deviceProperties = (BacnetDeviceProperties) device.getDeviceProperties();
        // 点位配置属性
        List<DevicePoint> devicePoints = device.getDevicePoints().values().stream()
                .filter(in -> in.getPointProperties() instanceof BacnetObjectProperty)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(devicePoints)) {
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

    public void readPropertiesToFile(Device device) {
        BacnetReadPropertyMultipleQuery query = makeQueryOf(device).orElse(null);
        this.restTemplate.postForLocation(this.bacnetServiceUrl + "/bacnet/readpropmToFile", query);
    }

    public static Optional<BacnetReadPropertyMultipleQuery> makeQueryOf(Device device) {
        if (device == null) {
            return Optional.empty();
        }
        // 设备配置属性
        BacnetDeviceProperties deviceProperties = (BacnetDeviceProperties) device.getDeviceProperties();
        // 点位配置属性
        List<DevicePoint> devicePoints = device.getDevicePoints().values().stream()
                .filter(in -> in.getPointProperties() instanceof BacnetObjectProperty)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(devicePoints)) {
            return Optional.empty();
        }

        Map<BacnetObject, List<BacnetProperty>> objectProperties = devicePoints.stream()
                .map(in -> (BacnetObjectProperty) in.getPointProperties())
                .collect(Collectors.groupingBy(BacnetObjectProperty::toBacnetObject, Collectors.mapping(BacnetObjectProperty::toBacnetProperty, Collectors.toList())));

        return Optional.of(new BacnetReadPropertyMultipleQuery(deviceProperties.getDeviceInstance(), objectProperties));
    }
}
