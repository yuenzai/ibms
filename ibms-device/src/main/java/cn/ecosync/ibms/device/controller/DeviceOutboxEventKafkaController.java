//package cn.ecosync.ibms.device.controller;
//
//import cn.ecosync.ibms.device.event.DeviceDataAcquisitionEvent;
//import cn.ecosync.ibms.device.event.DeviceEvent;
//import cn.ecosync.ibms.device.event.DeviceGatewayEvent;
//import cn.ecosync.iframework.event.Event;
//import cn.ecosync.iframework.exception.SerializationException;
//import cn.ecosync.iframework.serde.JsonSerde;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.ApplicationEventPublisherAware;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
//import static cn.ecosync.ibms.Constants.*;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DeviceOutboxEventKafkaController implements ApplicationEventPublisherAware {
//    private final JsonSerde jsonSerde;
//    private ApplicationEventPublisher eventPublisher;
//
//    @KafkaListener(topicPattern = "[\\w-_]*outbox-device[\\w-_]*")
//    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        log.info("onMessage[{}]", record);
//        try {
//            String topic = record.topic();
//            Event event = null;
//            if (topic.contains(AGGREGATE_TYPE_DEVICE)) {
//                event = jsonSerde.deserialize(record.value(), DeviceEvent.class);
//            } else if (topic.contains(AGGREGATE_TYPE_DEVICE_DAQ)) {
//                event = jsonSerde.deserialize(record.value(), DeviceDataAcquisitionEvent.class);
//            } else if (topic.contains(AGGREGATE_TYPE_DEVICE_GATEWAY)) {
//                event = jsonSerde.deserialize(record.value(), DeviceGatewayEvent.class);
//            }
//            if (event != null) eventPublisher.publishEvent(event);
//        } catch (SerializationException e) {
//            log.error("", e);
//        } finally {
//            ack.acknowledge();
//        }
//    }
//
//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
//        this.eventPublisher = eventPublisher;
//    }
//}
