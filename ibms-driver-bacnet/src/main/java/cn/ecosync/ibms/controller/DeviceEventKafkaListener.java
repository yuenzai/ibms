package cn.ecosync.ibms.controller;

import cn.ecosync.ibms.BacnetMapper;
import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.dto.ReadPropertyMultipleAck;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.device.command.CollectDeviceMetricCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionProperties;
import cn.ecosync.ibms.device.model.DeviceExtra;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.serde.JsonSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.Assert;

import java.util.Set;

@Slf4j
public class DeviceEventKafkaListener {
    private final String topicPrefix;
    private final QueryBus queryBus;
    private final JsonSerde jsonSerde;

    public DeviceEventKafkaListener(String topicPrefix, QueryBus queryBus, JsonSerde jsonSerde) {
        this.topicPrefix = topicPrefix;
        this.queryBus = queryBus;
        this.jsonSerde = jsonSerde;
    }

    @KafkaListener(
            id = "bacnet",
            topics = "#{__listener.topicPrefix + '-' + T(cn.ecosync.ibms.Constants).TOPIC_COLLECT_DEVICE_METRIC_COMMAND_JOINED}"
    )
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("onMessage[{}]", record);
        try {
            CollectDeviceMetricCommand cmd = jsonSerde.deserialize(record.value(), CollectDeviceMetricCommand.class);
            if (cmd == null) {
                return;
            }
            onMessage(cmd);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            ack.acknowledge();
        }
    }

    private void onMessage(CollectDeviceMetricCommand command) {
        Set<DeviceModel> devices = command.getAggregator().getDevices();
        DeviceDataAcquisitionModel daq = command.getDaq();
        Assert.notNull(daq, "daq must not be null");
        for (DeviceModel device : devices) {
            run(device, daq);
        }
    }

    private void run(DeviceModel device, DeviceDataAcquisitionModel daq) {
        Assert.isInstanceOf(DeviceExtra.BACnet.class, device.getDeviceExtra());
        Assert.isInstanceOf(DeviceDataAcquisitionProperties.BACnet.class, daq.getDaqProperties());

        DeviceExtra.BACnet deviceProperties = (DeviceExtra.BACnet) device.getDeviceExtra();
        DeviceDataAcquisitionProperties.BACnet daqProperties = (DeviceDataAcquisitionProperties.BACnet) daq.getDaqProperties();

        BacnetReadPropertyMultipleService readPropertyMultipleService = BacnetMapper.toReadPropertyMultipleService(deviceProperties, daqProperties);
        BacnetReadPropertyMultipleQuery query = new BacnetReadPropertyMultipleQuery(readPropertyMultipleService);
        ReadPropertyMultipleAck ack = queryBus.execute(query);
        if (ack == null) {
            return;
        }
        // todo send ack to kafka
    }
}
