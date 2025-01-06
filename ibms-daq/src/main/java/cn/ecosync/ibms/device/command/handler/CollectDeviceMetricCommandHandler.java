//package cn.ecosync.ibms.device.command.handler;
//
//import cn.ecosync.ibms.Topics;
//import cn.ecosync.ibms.bacnet.dto.*;
//import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleBatchQuery;
//import cn.ecosync.ibms.device.dto.CollectDeviceMetricDto;
//import cn.ecosync.ibms.device.dto.DeviceMetricDto;
//import cn.ecosync.ibms.device.event.DeviceMetricCollectedEvent;
//import cn.ecosync.ibms.device.model.*;
//import cn.ecosync.iframework.command.CommandHandler;
//import cn.ecosync.iframework.event.EventBus;
//import cn.ecosync.iframework.query.QueryBus;
//import cn.ecosync.iframework.util.CollectionUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//import org.springframework.util.MultiValueMap;
//
//import java.util.*;
//
//import static cn.ecosync.ibms.Topics.TopicEnum.TOPIC_DEVICE_METRIC_COLLECTED_EVENT;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class CollectDeviceMetricCommandHandler implements CommandHandler<CollectDeviceMetricDto> {
//    private final Topics topics;
//    private final QueryBus queryBus;
//    private final EventBus eventBus;
//
//    @Override
//    public void handle(CollectDeviceMetricDto command) {
//        Collection<DeviceModel> devices = command.getAggregator().getDevices();
//        DeviceDataAcquisitionModel daq = command.getDaq();
//        Assert.notNull(daq, "daq must not be null");
//        DeviceDataAcquisitionProperties daqProperties = daq.getDaqProperties();
//        if (daqProperties instanceof DeviceDataAcquisitionProperties.BACnet) {
//            DeviceDataAcquisitionProperties.BACnet bacnetProperties = (DeviceDataAcquisitionProperties.BACnet) daqProperties;
//            handleByBacnet(devices, bacnetProperties);
//        }
//    }
//
//    private void handleByBacnet(Collection<DeviceModel> devices, DeviceDataAcquisitionProperties.BACnet bacnetProperties) {
//        Map<Integer, DeviceModel> deviceMap = CollectionUtils.newHashMap(devices.size());
//        List<BacnetReadPropertyMultipleService> services = new ArrayList<>(devices.size());
//        for (DeviceModel device : devices) {
//            Integer deviceInstance = toDeviceInstance(device.getDeviceId());
//            if (deviceInstance == null) continue;
//            deviceMap.put(deviceInstance, device);
//            BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, bacnetProperties.getSchemas());
//            services.add(service);
//        }
//        BacnetReadPropertyMultipleBatchQuery query = new BacnetReadPropertyMultipleBatchQuery(services);
//        List<ReadPropertyMultipleAck> acks = queryBus.execute(query);
//        for (ReadPropertyMultipleAck ack : acks) {
//            Integer deviceInstance = ack.getDeviceInstance();
//            DeviceModel device = deviceMap.get(deviceInstance);
//            Map<String, DeviceMetricDto> metrics = ackToMetrics(ack, bacnetProperties.getSchemas());
//            DeviceMetricCollectedEvent event = new DeviceMetricCollectedEvent(
//                    topics.getTopicName(TOPIC_DEVICE_METRIC_COLLECTED_EVENT),
//                    device.getDeviceId(), metrics);
//            eventBus.publish(event);
//        }
//    }
//
//    private Integer toDeviceInstance(DeviceId deviceId) {
//        try {
//            String sid = DeviceId.extractPathVariable(deviceId.toStringId(), DeviceId.KEY_SID);
//            return Integer.parseInt(sid);
//        } catch (Exception e) {
//            log.error("", e);
//            return null;
//        }
//    }
//
//    private static Map<String, DeviceMetricDto> ackToMetrics(ReadPropertyMultipleAck ack, Collection<BacnetObjectPropertiesWithName> collection) {
//        MultiValueMap<BacnetObject, BacnetPropertyResult> multiValueMap = ack.toMultiValueMap();
//        Map<String, DeviceMetricDto> values = new HashMap<>();
//        for (BacnetObjectPropertiesWithName bops : collection) {
//            String name = bops.getName();
//            BacnetObject bo = bops.getBacnetObject();
//            // 一个 BacnetObject 可以有多个属性值
//            List<BacnetPropertyResult> bacnetPropertyValues = multiValueMap.get(bo);
//            DeviceMetricDto value = toMetric(bacnetPropertyValues);
//            if (value == null) continue;
//            values.put(name, value);
//        }
//        return values;
//    }
//
//    private static DeviceMetricDto toMetric(List<BacnetPropertyResult> bacnetPropertyValues) {
//        if (CollectionUtils.isEmpty(bacnetPropertyValues)) return null;
//        // 遍历 BacnetObject 的每个 property
//        if (bacnetPropertyValues.size() == 1) {
//            Object presentValue = formatValue(bacnetPropertyValues.get(0));
//            return new DeviceMetricDto(presentValue);
//        } else {
//            Object presentValue = null;
//            Map<String, Object> extraValues = CollectionUtils.newHashMap(bacnetPropertyValues.size());
//            for (BacnetPropertyResult bacnetPropertyValue : bacnetPropertyValues) {
//                Object value = formatValue(bacnetPropertyValue);
//                if (BacnetPropertyId.PROP_PRESENT_VALUE == bacnetPropertyValue.getProperty().getPropertyIdentifier()) {
//                    presentValue = value;
//                } else {
//                    extraValues.put(bacnetPropertyValue.getProperty().toStringId(), value);
//                }
//            }
//            return new DeviceMetricDto(presentValue, extraValues);
//        }
//    }
//
//    private static Object formatValue(Collection<BacnetPropertyValue> collection) {
//        return CollectionUtils.oneOrMore(new ArrayList<>(collection));
//    }
//}
