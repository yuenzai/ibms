package cn.ecosync.ibms.system.repository;

import cn.ecosync.ibms.system.model.Dictionary;
import cn.ecosync.ibms.system.model.DictionaryKey;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface DictionaryRepository {
    void put(Dictionary dictionary);

    void remove(Dictionary dictionary);

    default void remove(DictionaryKey key) {
        get(key).ifPresent(this::remove);
    }

    Optional<Dictionary> get(DictionaryKey key);
}
