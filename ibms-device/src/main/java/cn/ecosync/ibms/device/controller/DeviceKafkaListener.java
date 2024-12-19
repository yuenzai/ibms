package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.Topics;
import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleBatchQuery;
import cn.ecosync.ibms.device.command.CollectDeviceMetricCommand;
import cn.ecosync.ibms.device.event.DeviceMetricCollectedEvent;
import cn.ecosync.ibms.device.model.*;
import cn.ecosync.iframework.event.EventBus;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.util.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static cn.ecosync.ibms.Topics.TopicEnum.TOPIC_DEVICE_METRIC_COLLECTED_EVENT;

public class DeviceKafkaListener {
    private static final Logger log = LoggerFactory.getLogger(DeviceKafkaListener.class);

    private final Topics topics;
    private final QueryBus queryBus;
    private final JsonSerde jsonSerde;
    private final EventBus eventBus;

    public DeviceKafkaListener(Topics topics, QueryBus queryBus, JsonSerde jsonSerde, EventBus eventBus) {
        this.topics = topics;
        this.queryBus = queryBus;
        this.jsonSerde = jsonSerde;
        this.eventBus = eventBus;
    }

    @KafkaListener(topicPattern = "[\\w-_]*collect-device-metric-command-joined")
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("onMessage[{}]", record);
        try {
            CollectDeviceMetricCommand cmd = jsonSerde.deserialize(record.value(), CollectDeviceMetricCommand.class);
            Assert.notNull(cmd, "CollectDeviceMetricCommand must not be null");
            dispatch(cmd);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            ack.acknowledge();
        }
    }

    private void dispatch(CollectDeviceMetricCommand command) {
        Set<DeviceModel> devices = command.getAggregator().getDevices();
        DeviceDataAcquisitionModel daq = command.getDaq();
        Assert.notNull(daq, "daq must not be null");
        DeviceDataAcquisitionProperties daqProperties = daq.getDaqProperties();
        if (daqProperties instanceof DeviceDataAcquisitionProperties.BACnet) {
            DeviceDataAcquisitionProperties.BACnet bacnetProperties = (DeviceDataAcquisitionProperties.BACnet) daqProperties;
            handleByBacnet(devices, bacnetProperties);
        }
    }

    private void handleByBacnet(Set<DeviceModel> devices, DeviceDataAcquisitionProperties.BACnet bacnetProperties) {
        Map<Integer, DeviceModel> deviceMap = CollectionUtils.newHashMap(devices.size());
        List<BacnetReadPropertyMultipleService> services = new ArrayList<>(devices.size());
        for (DeviceModel device : devices) {
            Integer deviceInstance = toDeviceInstance(device.getDeviceId());
            if (deviceInstance == null) continue;
            deviceMap.put(deviceInstance, device);
            BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, bacnetProperties.getFields());
            services.add(service);
        }
        BacnetReadPropertyMultipleBatchQuery query = new BacnetReadPropertyMultipleBatchQuery(services);
        List<ReadPropertyMultipleAck> acks = queryBus.execute(query);
        for (ReadPropertyMultipleAck ack : acks) {
            Integer deviceInstance = ack.getDeviceInstance();
            DeviceModel device = deviceMap.get(deviceInstance);
            Map<String, DeviceMetric> metrics = ackToMetrics(ack, bacnetProperties.getFields());
            DeviceMetricCollectedEvent event = new DeviceMetricCollectedEvent(
                    topics.getTopicName(TOPIC_DEVICE_METRIC_COLLECTED_EVENT),
                    device.getDeviceId(), device.getDaqId(), System.currentTimeMillis(), metrics);
            eventBus.publish(event);
        }
    }

    private Integer toDeviceInstance(DeviceId deviceId) {
        try {
            String sid = DeviceId.extractPathVariable(deviceId.toStringId(), DeviceId.KEY_SID);
            return Integer.parseInt(sid);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    private static Map<String, DeviceMetric> ackToMetrics(ReadPropertyMultipleAck ack, Collection<BacnetObjectPropertiesWithName> collection) {
        MultiValueMap<BacnetObject, BacnetPropertyValues> multiValueMap = ack.toMultiValueMap();
        Map<String, DeviceMetric> values = new HashMap<>();
        for (BacnetObjectPropertiesWithName bops : collection) {
            String name = bops.getName();
            BacnetObject bo = bops.getBacnetObject();
            // 一个 BacnetObject 可以有多个属性值
            List<BacnetPropertyValues> bacnetPropertyValues = multiValueMap.get(bo);
            DeviceMetric value = toMetric(bacnetPropertyValues);
            if (value == null) continue;
            values.put(name, value);
        }
        return values;
    }

    private static DeviceMetric toMetric(List<BacnetPropertyValues> bacnetPropertyValues) {
        if (CollectionUtils.isEmpty(bacnetPropertyValues)) return null;
        // 遍历 BacnetObject 的每个 property
        if (bacnetPropertyValues.size() == 1) {
            Object presentValue = formatValue(bacnetPropertyValues.get(0));
            return new DeviceMetric(presentValue);
        } else {
            Object presentValue = null;
            Map<String, Object> extraValues = CollectionUtils.newHashMap(bacnetPropertyValues.size());
            for (BacnetPropertyValues bacnetPropertyValue : bacnetPropertyValues) {
                Object value = formatValue(bacnetPropertyValue);
                if (BacnetPropertyId.PROP_PRESENT_VALUE == bacnetPropertyValue.getProperty().getPropertyIdentifier()) {
                    presentValue = value;
                } else {
                    extraValues.put(bacnetPropertyValue.getProperty().toStringId(), value);
                }
            }
            return new DeviceMetric(presentValue, extraValues);
        }
    }

    private static Object formatValue(Collection<BacnetPropertyValue> collection) {
        return CollectionUtils.oneOrMore(new ArrayList<>(collection));
    }
}
