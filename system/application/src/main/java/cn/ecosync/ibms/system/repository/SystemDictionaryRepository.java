package cn.ecosync.ibms.system.repository;

import cn.ecosync.ibms.system.model.SystemDictionary;
import cn.ecosync.ibms.system.model.SystemDictionaryKey;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface SystemDictionaryRepository {
    void put(SystemDictionary systemDictionary);

    void remove(SystemDictionary systemDictionary);

    default void remove(SystemDictionaryKey key) {
        get(key).ifPresent(this::remove);
    }

    Optional<SystemDictionary> get(SystemDictionaryKey key);
}
