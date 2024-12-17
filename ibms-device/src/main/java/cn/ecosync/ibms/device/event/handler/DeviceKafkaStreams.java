package cn.ecosync.ibms.device.event.handler;

import cn.ecosync.ibms.Topics;
import cn.ecosync.ibms.device.command.CollectDeviceMetricCommand;
import cn.ecosync.ibms.device.event.AbstractDeviceEvent;
import cn.ecosync.ibms.device.event.DeviceDataAcquisitionEvent;
import cn.ecosync.ibms.device.event.DeviceEventAggregator;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionDto;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.model.DeviceId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.ParseStringDeserializer;
import org.springframework.kafka.support.serializer.ToFromStringSerde;
import org.springframework.kafka.support.serializer.ToStringSerializer;

import static cn.ecosync.ibms.Topics.TopicEnum.*;
import static org.apache.kafka.streams.Topology.AutoOffsetReset.EARLIEST;
import static org.apache.kafka.streams.Topology.AutoOffsetReset.LATEST;

public class DeviceKafkaStreams implements InitializingBean {
    public static final Logger log = LoggerFactory.getLogger(DeviceKafkaStreams.class);

    private final Topics topics;
    private final StreamsBuilder streamsBuilder;

    private final Serde<DeviceId> deviceIdSerde;
    private final Serde<AbstractDeviceEvent> deviceSerde;
    private final Serde<DeviceDataAcquisitionId> daqIdSerde;
    private final Serde<DeviceDataAcquisitionEvent> daqEventSerde;
    private final Serde<DeviceDataAcquisitionModel> daqSerde;
    private final Serde<DeviceEventAggregator> deviceEventAggregatorSerde;

    private final Serde<CollectDeviceMetricCommand> collectDeviceMetricCommandSerde;

    private DeviceKafkaStreams(
            Topics topics,
            StreamsBuilder streamsBuilder,
            Serde<DeviceId> deviceIdSerde,
            Serde<AbstractDeviceEvent> deviceSerde,
            Serde<DeviceDataAcquisitionId> daqIdSerde,
            Serde<DeviceDataAcquisitionEvent> daqEventSerde,
            Serde<DeviceDataAcquisitionModel> daqSerde,
            Serde<DeviceEventAggregator> deviceEventAggregatorSerde,
            Serde<CollectDeviceMetricCommand> collectDeviceMetricCommandSerde) {
        this.topics = topics;
        this.streamsBuilder = streamsBuilder;
        this.deviceIdSerde = deviceIdSerde;
        this.deviceSerde = deviceSerde;
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
        KTable<DeviceDataAcquisitionId, DeviceDataAcquisitionModel> daqTable = daqTable();

        String collectDeviceMetricCommandTopic = topics.getTopicName(TOPIC_COLLECT_DEVICE_METRIC_COMMAND);
        Consumed<DeviceDataAcquisitionId, CollectDeviceMetricCommand> collectDeviceMetricCommandConsumed = Consumed.with(daqIdSerde, collectDeviceMetricCommandSerde).withOffsetResetPolicy(LATEST);
        KStream<DeviceDataAcquisitionId, CollectDeviceMetricCommand> collectDeviceMetricCommandStream = streamsBuilder.stream(collectDeviceMetricCommandTopic, collectDeviceMetricCommandConsumed);

        String collectDeviceMetricCommandJoinedTopic = topics.getTopicName(TOPIC_COLLECT_DEVICE_METRIC_COMMAND_JOINED);
        collectDeviceMetricCommandStream
                .join(daqTable, CollectDeviceMetricCommand::withDaq, Joined.with(daqIdSerde, collectDeviceMetricCommandSerde, daqSerde))
                .join(devicesTable, CollectDeviceMetricCommand::withAggregator, Joined.with(daqIdSerde, collectDeviceMetricCommandSerde, deviceEventAggregatorSerde))
                .to(collectDeviceMetricCommandJoinedTopic, Produced.with(daqIdSerde, collectDeviceMetricCommandSerde));
    }

    private KTable<DeviceDataAcquisitionId, DeviceEventAggregator> devicesTable() {
        String deviceTopic = topics.getTopicName(TOPIC_DEVICE);
        Consumed<DeviceId, AbstractDeviceEvent> deviceConsumed = Consumed.with(deviceIdSerde, deviceSerde).withOffsetResetPolicy(EARLIEST);
        KTable<DeviceId, AbstractDeviceEvent> deviceTable = streamsBuilder.table(deviceTopic, deviceConsumed);

        return deviceTable.toStream()
                .filter(DeviceEventAggregator::canApply)
                .selectKey((k, v) -> v.device().get().getDaqId())
                .groupByKey(Grouped.with(daqIdSerde, deviceSerde))
                .aggregate(DeviceEventAggregator::newInstance, DeviceEventAggregator::aggregator, Materialized.with(daqIdSerde, deviceEventAggregatorSerde));
    }

    private KTable<DeviceDataAcquisitionId, DeviceDataAcquisitionModel> daqTable() {
        String daqTopic = topics.getTopicName(TOPIC_AGGREGATE_TYPE_DEVICE_DAQ);
        Consumed<DeviceDataAcquisitionId, DeviceDataAcquisitionEvent> daqConsumed = Consumed.with(daqIdSerde, daqEventSerde).withOffsetResetPolicy(EARLIEST);
        return streamsBuilder.table(daqTopic, daqConsumed)
//                .filter((k, v) -> v instanceof DeviceSchemaSavedEvent || v instanceof DeviceSchemaRemovedEvent)
                .mapValues((k, v) -> v.daq().orElse(null))
                .filter((k, v) -> v != null);
    }

    public static DeviceKafkaStreams newInstance(Topics topics, StreamsBuilder streamsBuilder, ObjectMapper objectMapper) {
        return new DeviceKafkaStreams(
                topics,
                streamsBuilder,
                new ToFromStringSerde<>(new ToStringSerializer<>(), new ParseStringDeserializer<>(str -> new DeviceId(str))),
                new JsonSerde<>(AbstractDeviceEvent.class),
                new ToFromStringSerde<>(new ToStringSerializer<>(), new ParseStringDeserializer<>(DeviceDataAcquisitionId::new)),
                new JsonSerde<>(DeviceDataAcquisitionEvent.class),
                new JsonSerde<>(objectMapper.getTypeFactory().constructType(DeviceDataAcquisitionDto.class)),
                new JsonSerde<>(DeviceEventAggregator.class),
                new JsonSerde<>(CollectDeviceMetricCommand.class)
        );
    }
}
