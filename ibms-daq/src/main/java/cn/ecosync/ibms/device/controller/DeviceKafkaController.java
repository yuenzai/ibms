//package cn.ecosync.ibms.device.controller;
//
//import cn.ecosync.ibms.device.dto.CollectDeviceMetricDto;
//import cn.ecosync.iframework.command.CommandBus;
//import cn.ecosync.iframework.serde.JsonSerde;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.util.Assert;
//
//public class DeviceKafkaController {
//    private static final Logger log = LoggerFactory.getLogger(DeviceKafkaController.class);
//
//    private final CommandBus commandBus;
//    private final JsonSerde jsonSerde;
//
//    public DeviceKafkaController(CommandBus commandBus, JsonSerde jsonSerde) {
//        this.commandBus = commandBus;
//        this.jsonSerde = jsonSerde;
//    }
//
//    @KafkaListener(topicPattern = "[\\w-_]*collect-device-metric-enhanced-command")
//    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        log.info("onMessage[{}]", record);
//        try {
//            CollectDeviceMetricDto command = jsonSerde.deserialize(record.value(), CollectDeviceMetricDto.class);
//            Assert.notNull(command, "CollectDeviceMetricCommand must not be null");
//            commandBus.execute(command);
//        } catch (Exception e) {
//            log.error("", e);
//        } finally {
//            ack.acknowledge();
//        }
//    }
//}
