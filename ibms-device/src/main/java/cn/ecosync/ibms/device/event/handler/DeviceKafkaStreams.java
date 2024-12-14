package cn.ecosync.ibms.device.event.handler;

import cn.ecosync.ibms.device.command.CollectDeviceMetricCommand;
import cn.ecosync.ibms.device.event.AbstractDeviceEvent;
import cn.ecosync.ibms.device.event.DeviceDataAcquisitionEvent;
import cn.ecosync.ibms.device.event.DeviceEventAggregator;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionDto;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.iframework.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.ParseStringDeserializer;
import org.springframework.kafka.support.serializer.ToFromStringSerde;
import org.springframework.kafka.support.serializer.ToStringSerializer;

import static cn.ecosync.ibms.Constants.*;
import static org.apache.kafka.streams.Topology.AutoOffsetReset.EARLIEST;
import static org.apache.kafka.streams.Topology.AutoOffsetReset.LATEST;

@Slf4j
public class DeviceKafkaStreams implements InitializingBean {
    private final StreamsBuilder streamsBuilder;
    private final String topicPrefix;

    private final Serde<DeviceId> deviceIdSerde;
    private final Serde<AbstractDeviceEvent> deviceSerde;
    //    private final Serde<DeviceSchemaId> deviceSchemaIdSerde;
//    private final Serde<DeviceSchemaEvent> deviceSchemaEventSerde;
//    private final Serde<DeviceSchemaQueryModel> deviceSchemaSerde;
    private final Serde<DeviceDataAcquisitionId> daqIdSerde;
    private final Serde<DeviceDataAcquisitionEvent> daqEventSerde;
    private final Serde<DeviceDataAcquisitionModel> daqSerde;
    private final Serde<DeviceEventAggregator> deviceEventAggregatorSerde;

    private final Serde<CollectDeviceMetricCommand> collectDeviceMetricCommandSerde;

    private DeviceKafkaStreams(
            StreamsBuilder streamsBuilder,
            String topicPrefix,
            Serde<DeviceId> deviceIdSerde,
            Serde<AbstractDeviceEvent> deviceSerde,
//            Serde<DeviceSchemaId> deviceSchemaIdSerde,
//            Serde<DeviceSchemaEvent> deviceSchemaEventSerde,
//            Serde<DeviceSchemaQueryModel> deviceSchemaSerde,
            Serde<DeviceDataAcquisitionId> daqIdSerde,
            Serde<DeviceDataAcquisitionEvent> daqEventSerde,
            Serde<DeviceDataAcquisitionModel> daqSerde,
            Serde<DeviceEventAggregator> deviceEventAggregatorSerde,
            Serde<CollectDeviceMetricCommand> collectDeviceMetricCommandSerde) {
        this.streamsBuilder = streamsBuilder;
        this.topicPrefix = topicPrefix;
        this.deviceIdSerde = deviceIdSerde;
        this.deviceSerde = deviceSerde;
//        this.deviceSchemaIdSerde = deviceSchemaIdSerde;
//        this.deviceSchemaEventSerde = deviceSchemaEventSerde;
//        this.deviceSchemaSerde = deviceSchemaSerde;
        this.daqIdSerde = daqIdSerde;
        this.daqEventSerde = daqEventSerde;
        this.daqSerde = daqSerde;
        this.deviceEventAggregatorSerde = deviceEventAggregatorSerde;
        this.collectDeviceMetricCommandSerde = collectDeviceMetricCommandSerde;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            afterPropertiesSetImpl();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void afterPropertiesSetImpl() {
        KTable<DeviceDataAcquisitionId, DeviceEventAggregator> devicesTable = devicesTable();
//        KTable<DeviceSchemaId, DeviceSchemaQueryModel> deviceSchemaTable = deviceSchemaTable();
        KTable<DeviceDataAcquisitionId, DeviceDataAcquisitionModel> daqTable = daqTable();

        String collectDeviceMetricCommandTopic = toFullName(TOPIC_COLLECT_DEVICE_METRIC_COMMAND);
        Consumed<DeviceDataAcquisitionId, CollectDeviceMetricCommand> collectDeviceMetricCommandConsumed = Consumed.with(daqIdSerde, collectDeviceMetricCommandSerde).withOffsetResetPolicy(LATEST);
        KStream<DeviceDataAcquisitionId, CollectDeviceMetricCommand> collectDeviceMetricCommandStream = streamsBuilder.stream(collectDeviceMetricCommandTopic, collectDeviceMetricCommandConsumed);

        String collectDeviceMetricCommandJoinedTopic = toFullName(TOPIC_COLLECT_DEVICE_METRIC_COMMAND_JOINED);
        collectDeviceMetricCommandStream
                .join(daqTable, CollectDeviceMetricCommand::withDaq, Joined.with(daqIdSerde, collectDeviceMetricCommandSerde, daqSerde))
                .join(devicesTable, CollectDeviceMetricCommand::withAggregator, Joined.with(daqIdSerde, collectDeviceMetricCommandSerde, deviceEventAggregatorSerde))
                .to(collectDeviceMetricCommandJoinedTopic, Produced.with(daqIdSerde, collectDeviceMetricCommandSerde));
    }

    private KTable<DeviceDataAcquisitionId, DeviceEventAggregator> devicesTable() {
        String deviceTopic = toFullName(TOPIC_DEVICE);
        Consumed<DeviceId, AbstractDeviceEvent> deviceConsumed = Consumed.with(deviceIdSerde, deviceSerde).withOffsetResetPolicy(EARLIEST);
        KTable<DeviceId, AbstractDeviceEvent> deviceTable = streamsBuilder.table(deviceTopic, deviceConsumed);

        return deviceTable.toStream()
                .filter(DeviceEventAggregator::canApply)
                .selectKey((k, v) -> v.device().get().getDaqId())
                .groupByKey(Grouped.with(daqIdSerde, deviceSerde))
                .aggregate(DeviceEventAggregator::newInstance, DeviceEventAggregator::aggregator, Materialized.with(daqIdSerde, deviceEventAggregatorSerde));
    }

    private KTable<DeviceDataAcquisitionId, DeviceDataAcquisitionModel> daqTable() {
        String daqTopic = toFullName(TOPIC_AGGREGATE_TYPE_DEVICE_DAQ);
        Consumed<DeviceDataAcquisitionId, DeviceDataAcquisitionEvent> daqConsumed = Consumed.with(daqIdSerde, daqEventSerde).withOffsetResetPolicy(EARLIEST);
        return streamsBuilder.table(daqTopic, daqConsumed)
//                .filter((k, v) -> v instanceof DeviceSchemaSavedEvent || v instanceof DeviceSchemaRemovedEvent)
                .mapValues((k, v) -> v.daq().orElse(null))
                .filter((k, v) -> v != null);
    }

//    private KTable<DeviceSchemaId, DeviceSchemaQueryModel> deviceSchemaTable() {
//        String deviceSchemaTopic = toFullName(TOPIC_DEVICE_SCHEMA);
//        Consumed<DeviceSchemaId, DeviceSchemaEvent> deviceSchemaConsumed = Consumed.with(deviceSchemaIdSerde, deviceSchemaEventSerde).withOffsetResetPolicy(EARLIEST);
//        return streamsBuilder.table(deviceSchemaTopic, deviceSchemaConsumed)
//                .filter((k, v) -> v instanceof DeviceSchemaSavedEvent || v instanceof DeviceSchemaRemovedEvent)
//                .mapValues((k, v) -> v.deviceSchema().orElse(null))
//                .filter((k, v) -> v != null);
//    }

    private String toFullName(String key) {
        if (StringUtils.hasText(topicPrefix)) {
            return String.format("%s-%s", topicPrefix, key);
        } else {
            return key;
        }
    }

    public static DeviceKafkaStreams newInstance(StreamsBuilder streamsBuilder, String topicPrefix, ObjectMapper objectMapper) {
        return new DeviceKafkaStreams(
                streamsBuilder,
                topicPrefix,
                new ToFromStringSerde<>(new ToStringSerializer<>(), new ParseStringDeserializer<>(DeviceId::new)),
                new JsonSerde<>(AbstractDeviceEvent.class),
//                new ToFromStringSerde<>(new ToStringSerializer<>(), new ParseStringDeserializer<>(DeviceSchemaId::new)),
//                new JsonSerde<>(DeviceSchemaEvent.class),
//                new JsonSerde<>(objectMapper.getTypeFactory().constructType(DeviceSchemaDto.class)),
                new ToFromStringSerde<>(new ToStringSerializer<>(), new ParseStringDeserializer<>(DeviceDataAcquisitionId::new)),
                new JsonSerde<>(DeviceDataAcquisitionEvent.class),
                new JsonSerde<>(objectMapper.getTypeFactory().constructType(DeviceDataAcquisitionDto.class)),
                new JsonSerde<>(DeviceEventAggregator.class),
                new JsonSerde<>(CollectDeviceMetricCommand.class)
        );
    }
}
