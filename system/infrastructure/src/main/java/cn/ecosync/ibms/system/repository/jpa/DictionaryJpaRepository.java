package cn.ecosync.ibms.system.repository.jpa;

import cn.ecosync.ibms.system.model.Dictionary;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.repository.DictionaryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DictionaryJpaRepository extends JpaRepository<Dictionary, Integer>, DictionaryRepository {
    @Override
    default void put(Dictionary dictionary) {
        save(dictionary);
    }

    @Override
    default void remove(Dictionary dictionary) {
        delete(dictionary);
    }

    @Override
    default Optional<Dictionary> get(DictionaryKey key) {
        return findByKey(key);
    }

    Optional<Dictionary> findByKey(DictionaryKey key);
}
