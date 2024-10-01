package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.dto.DeviceStatusDto;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointValue;
import cn.ecosync.ibms.device.model.bacnet.*;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import cn.ecosync.ibms.device.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.device.query.GetDeviceStatusQuery;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(GetDeviceStatusQueryHandler.BACNET_SERVICE_URL)
public class GetDeviceStatusQueryHandler implements QueryHandler<GetDeviceStatusQuery, Optional<DeviceStatusDto>> {
    public static final String BACNET_SERVICE_URL = "BACNET_SERVICE_URL";

    private final DeviceRepository deviceRepository;
    private final RestTemplate restTemplate;
    private final String bacnetServiceUrl;

    public GetDeviceStatusQueryHandler(DeviceRepository deviceRepository, RestTemplateBuilder builder, Environment environment) {
        this.deviceRepository = deviceRepository;
        this.restTemplate = builder.setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        bacnetServiceUrl = environment.getRequiredProperty(BACNET_SERVICE_URL);
    }

    @Override
    public Optional<DeviceStatusDto> handle(GetDeviceStatusQuery query) {
        DeviceId deviceId = query.toDeviceId();
        Device device = deviceRepository.get(deviceId).orElse(null);
        if (device == null) {
            return Optional.empty();
        }
        try {
            if (device.getDeviceProperties() instanceof BacnetDeviceProperties) {
                Map<DevicePoint, DevicePointValue> properties = handleByBacnet(device);
                return Optional.of(new DeviceStatusDto(device, properties));
            }
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
        return Optional.empty();
    }

    private Map<DevicePoint, DevicePointValue> handleByBacnet(Device device) {
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
