package cn.ecosync.ibms.device.event.handler;

import cn.ecosync.ibms.device.command.ReadDeviceMetricCommand;
import cn.ecosync.ibms.device.command.ReadDeviceMetricWithSchemaCommand;
import cn.ecosync.ibms.device.dto.DeviceWithSchemaDto;
import cn.ecosync.ibms.device.event.DeviceStatusUploadedEvent;
import cn.ecosync.ibms.device.model.*;
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

    private final Serde<DeviceId> deviceKeySerde;
    private final Serde<DeviceQueryModel> deviceValueSerde;
    private final Serde<DeviceSchemaId> deviceSchemaKeySerde;
    private final Serde<DeviceSchemaQueryModel> deviceSchemaValueSerde;
    private final Serde<ReadDeviceMetricCommand> readDeviceMetricCommandSerde;
    private final Serde<DeviceWithSchemaDto> deviceWithSchemaSerde;
    private final Serde<ReadDeviceMetricWithSchemaCommand> readDeviceMetricWithSchemaCommandSerde;
    private final Serde<DeviceStatusUploadedEvent> deviceStatusUploadedEventSerde;

    public DeviceKafkaStreams(StreamsBuilder streamsBuilder, ObjectMapper objectMapper, String topicPrefix) {
        this.streamsBuilder = streamsBuilder;
        this.topicPrefix = topicPrefix;
        this.deviceKeySerde = new ToFromStringSerde<>(new ToStringSerializer<>(), new ParseStringDeserializer<>(DeviceId::new));
        this.deviceValueSerde = new JsonSerde<>(objectMapper.getTypeFactory().constructType(Device.class));
        this.deviceSchemaKeySerde = new ToFromStringSerde<>(new ToStringSerializer<>(), new ParseStringDeserializer<>(DeviceSchemaId::new));
        this.deviceSchemaValueSerde = new JsonSerde<>(objectMapper.getTypeFactory().constructType(DeviceSchema.class));
        this.readDeviceMetricCommandSerde = new JsonSerde<>(ReadDeviceMetricCommand.class);
        this.deviceWithSchemaSerde = new JsonSerde<>(DeviceWithSchemaDto.class);
        this.readDeviceMetricWithSchemaCommandSerde = new JsonSerde<>(ReadDeviceMetricWithSchemaCommand.class);
        this.deviceStatusUploadedEventSerde = new JsonSerde<>(DeviceStatusUploadedEvent.class);
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
        KTable<DeviceId, DeviceQueryModel> deviceTable = streamsBuilder.table(toFullName(TOPIC_DEVICE),
                Consumed.with(deviceKeySerde, deviceValueSerde).withOffsetResetPolicy(EARLIEST));

        KTable<DeviceSchemaId, DeviceSchemaQueryModel> deviceSchemaTable = streamsBuilder.table(toFullName(TOPIC_DEVICE_SCHEMA),
                Consumed.with(deviceSchemaKeySerde, deviceSchemaValueSerde).withOffsetResetPolicy(EARLIEST));

        KTable<DeviceId, DeviceWithSchemaDto> deviceWithSchemaTable = deviceTable.join(deviceSchemaTable,
                DeviceQueryModel::getDeviceSchemaId, DeviceWithSchemaDto::new, Materialized.with(deviceKeySerde, deviceWithSchemaSerde));

        streamsBuilder.stream(toFullName(TOPIC_READ_DEVICE_METRIC_COMMAND), Consumed.with(deviceKeySerde, readDeviceMetricCommandSerde).withOffsetResetPolicy(LATEST))
                .join(deviceWithSchemaTable, ReadDeviceMetricWithSchemaCommand::new, Joined.with(deviceKeySerde, readDeviceMetricCommandSerde, deviceWithSchemaSerde))
                .to(toFullName(TOPIC_READ_DEVICE_METRIC_WITH_SCHEMA_COMMAND), Produced.with(deviceKeySerde, readDeviceMetricWithSchemaCommandSerde));

        streamsBuilder.stream(toFullName(TOPIC_DEVICE_METRIC), Consumed.with(deviceKeySerde, deviceStatusUploadedEventSerde))
                .split();
    }

    private String toFullName(String key) {
        if (StringUtils.hasText(topicPrefix)) {
            return String.format("%s-%s", topicPrefix, key);
        } else {
            return key;
        }
    }
}
