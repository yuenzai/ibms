package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.BacnetApplicationService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetDeviceStatusQueryHandler implements QueryHandler<GetDeviceStatusQuery, Optional<DeviceStatusDto>> {
    private final DeviceRepository deviceRepository;
    private final BacnetApplicationService bacnetApplicationService;

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

    private Map<DevicePoint, DevicePointValue> handleByBacnet(Device device) throws IOException, InterruptedException {
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
        List<ReadPropertyMultipleAck> ack = bacnetApplicationService.handle(query);
        Map<BacnetObjectProperty, BacnetPropertyValue> simpleMap = ReadPropertyMultipleAck.toSimpleMap(ack);
        return devicePoints.stream()
                .collect(Collectors.toMap(Function.identity(), in -> simpleMap.get((BacnetObjectProperty) in.getPointProperties())));// 注意这里没有对 key 去重，如果重复会报错
    }
}
