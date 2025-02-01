package cn.ecosync.ibms;

import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.query.QueryHandler;

import java.util.function.Consumer;

public interface SynchronizationService<K, V, T extends Query<V>> extends QueryHandler<T, V> {
    void poll(K key, T query, Consumer<V> consumer);

    void notify(K key);
}
