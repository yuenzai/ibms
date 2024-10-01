package cn.ecosync.ibms;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import static org.apache.kafka.common.serialization.Serdes.String;

@Configuration
@EnableKafkaStreams
@ConditionalOnClass(KafkaStreams.class)
@ConditionalOnProperty(prefix = "spring.kafka", name = "bootstrap-servers")
public class KafkaStreamsConfiguration {
    /**
     * 消费某个主题，构建 GlobalStateStore
     * tip: GlobalStateStore 是只读的
     *
     * @param builder     StreamsBuilder
     * @param topic       要用来构建 GlobalStateStore 的 topic
     * @param storeName   GlobalStateStore 的名字，用的时候通过这个名字获取
     * @param valueSerde  值序列化器
     * @param resetPolicy 消费主题时，如果没有找到偏移量，从哪里开始消费
     */
    private <K, V> void addGlobalStore(StreamsBuilder builder, String topic, String storeName, Serde<K> keySerde, Serde<V> valueSerde, Topology.AutoOffsetReset resetPolicy) {
        KeyValueBytesStoreSupplier keyValueStoreSupplier = Stores.persistentKeyValueStore(storeName);
        StoreBuilder<KeyValueStore<K, V>> storeBuilder = Stores
                .keyValueStoreBuilder(keyValueStoreSupplier, keySerde, valueSerde);
        Consumed<K, V> consumed = Consumed.with(keySerde, valueSerde)
                .withOffsetResetPolicy(resetPolicy);
        builder.addGlobalStore(
                storeBuilder,
                topic,
                consumed,
                () -> new GlobalStoreUpdater<>(storeName)
        );
    }

    private <V> void addGlobalStore(StreamsBuilder builder, String topic, String storeName, Serde<V> valueSerde, Topology.AutoOffsetReset resetPolicy) {
        addGlobalStore(builder, topic, storeName, String(), valueSerde, resetPolicy);
    }

    /**
     * 添加 StateStore 到 Topology
     * tip: 通常用来在流处理中当 Map 使用
     *
     * @param builder    StreamsBuilder
     * @param storeName  StateStore 的名字，用的时候通过这个名字获取
     * @param valueSerde 值序列化器
     */
    private void addStore(StreamsBuilder builder, String storeName, Serde<?> valueSerde) {
        KeyValueBytesStoreSupplier keyValueStoreSupplier = Stores.persistentKeyValueStore(storeName);
        builder.addStateStore(Stores.keyValueStoreBuilder(keyValueStoreSupplier, Serdes.String(), valueSerde));
    }

    // Processor that keeps the global store updated.
    // https://github.com/confluentinc/kafka-streams-examples/blob/master/src/main/java/io/confluent/examples/streams/GlobalStoresExample.java
    @Slf4j
    public static class GlobalStoreUpdater<K, V> implements Processor<K, V, Void, Void> {
        private final String storeName;
        private KeyValueStore<K, V> store;

        public GlobalStoreUpdater(final String storeName) {
            this.storeName = storeName;
        }

        @Override
        public void init(final ProcessorContext<Void, Void> processorContext) {
            store = processorContext.getStateStore(storeName);
        }

        @Override
        public void process(final Record<K, V> record) {
            // We are only supposed to put operation the keep the store updated.
            // We should not filter record or modify the key or value
            // Doing so would break fault-tolerance.
            // see https://issues.apache.org/jira/browse/KAFKA-7663
            store.put(record.key(), record.value());
            log.info("globalStateStore更新[storeName={}, record={}]", storeName, record);
        }

        @Override
        public void close() {
            // No-op
        }
    }
}
