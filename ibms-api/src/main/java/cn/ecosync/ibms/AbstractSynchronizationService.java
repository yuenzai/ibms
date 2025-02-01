package cn.ecosync.ibms;

import cn.ecosync.ibms.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.function.Consumer;

@Slf4j
public abstract class AbstractSynchronizationService<K, V, T extends Query<V>> implements SynchronizationService<K, V, T> {
    public void poll(K key, T query, Consumer<V> consumer) {
        Assert.notNull(key, "key must not be null");
        Assert.notNull(query, "query must not be null");
        Assert.notNull(consumer, "consumer must not be null");
        V v = handle(query);
        if (v != null) {
            consumer.accept(v);
        } else {
            put(key, consumer);
        }
    }

    public void notify(K key) {
        Consumer<V> consumer = remove(key);
        if (consumer != null) {
            consumer.accept(null);
            log.info("唤醒异步请求[key={}]", key);
        }
    }

    protected abstract void put(K key, Consumer<V> value);

    protected abstract Consumer<V> remove(K key);
}
