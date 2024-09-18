package cn.ecosync.ibms.system.repository.jpa;

import cn.ecosync.ibms.system.model.SystemDictionary;
import cn.ecosync.ibms.system.model.SystemDictionaryKey;
import cn.ecosync.ibms.system.repository.SystemDictionaryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemDictionaryJpaRepository extends JpaRepository<SystemDictionary, Integer>, SystemDictionaryRepository {
    @Override
    default void put(SystemDictionary systemDictionary) {
        save(systemDictionary);
    }

    @Override
    default void remove(SystemDictionary systemDictionary) {
        delete(systemDictionary);
    }

    @Override
    default Optional<SystemDictionary> get(SystemDictionaryKey key) {
        return findByKey(key);
    }

    Optional<SystemDictionary> findByKey(SystemDictionaryKey key);
}
