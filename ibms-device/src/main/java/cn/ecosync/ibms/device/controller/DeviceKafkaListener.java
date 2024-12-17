package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.Topics;
import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleBatchQuery;
import cn.ecosync.ibms.device.command.CollectDeviceMetricCommand;
import cn.ecosync.ibms.device.event.DeviceMetricCollectedEvent;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionProperties;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.util.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.Topics.TopicEnum.TOPIC_DEVICE_METRIC_COLLECTED_EVENT;

public class DeviceKafkaListener {
    private static final Logger log = LoggerFactory.getLogger(DeviceKafkaListener.class);

    private final Topics topics;
    private final QueryBus queryBus;
    private final JsonSerde jsonSerde;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public DeviceKafkaListener(Topics topics, QueryBus queryBus, JsonSerde jsonSerde, KafkaTemplate<String, String> kafkaTemplate) {
        this.topics = topics;
        this.queryBus = queryBus;
        this.jsonSerde = jsonSerde;
        this.kafkaTemplate = kafkaTemplate;
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

            Map<Integer, DeviceModel> deviceMap = CollectionUtils.newHashMap(devices.size());
            List<BacnetReadPropertyMultipleService> services = new ArrayList<>(devices.size());
            for (DeviceModel device : devices) {
                Integer deviceInstance = toDeviceInstance(device.getDeviceId());
                if (deviceInstance == null) continue;
                deviceMap.put(deviceInstance, device);
                BacnetReadPropertyMultipleService service = BacnetReadPropertyMultipleService.newInstance(deviceInstance, bacnetProperties.getBacnetPoints(), BacnetObjectPropertyWithKey::getBop);
                services.add(service);
            }
            BacnetReadPropertyMultipleBatchQuery query = new BacnetReadPropertyMultipleBatchQuery(services);
            List<ReadPropertyMultipleAck> acks = queryBus.execute(query);
            for (ReadPropertyMultipleAck ack : acks) {
                Integer deviceInstance = ack.getDeviceInstance();
                DeviceModel device = deviceMap.get(deviceInstance);
                Map<String, Object> values = ackToMap(ack, bacnetProperties.getBacnetPoints(), BacnetObjectPropertyWithKey::getKey, BacnetObjectPropertyWithKey::getBop);
                DeviceMetricCollectedEvent event = new DeviceMetricCollectedEvent(device.getDeviceId(), device.getDaqId(), System.currentTimeMillis(), values);
                String payload = jsonSerde.serialize(event);
                kafkaTemplate.send(topics.getTopicName(TOPIC_DEVICE_METRIC_COLLECTED_EVENT), event.toStringId(), payload);
            }
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

    private static <K, T> Map<K, Object> ackToMap(ReadPropertyMultipleAck ack, Collection<T> collection, Function<T, K> keyFunction, Function<T, BacnetObjectProperty> function) {
        Map<K, Object> map = CollectionUtils.newHashMap(collection.size());
        MultiValueMap<BacnetObjectProperty, BacnetPropertyValue> valueMap = ack.flatMap();
        for (T element : collection) {
            K key = keyFunction.apply(element);
            BacnetObjectProperty bop = function.apply(element);
            List<Object> values = valueMap.getOrDefault(bop, Collections.emptyList()).stream()
                    .map(BacnetPropertyValue::getValue)
                    .collect(Collectors.toList());
            Object pointValue = CollectionUtils.oneOrMore(values);// null represent the error
            map.put(key, pointValue);
        }
        return map;
    }
}
