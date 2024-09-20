package cn.ecosync.ibms.system.repository.memory;

import cn.ecosync.ibms.system.model.SystemDictionary;
import cn.ecosync.ibms.system.model.SystemDictionaryKey;
import cn.ecosync.ibms.system.repository.SystemDictionaryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class SystemDictionaryMemoryRepository implements SystemDictionaryRepository {
    @Override
    public void put(SystemDictionary systemDictionary) {

    }

    @Override
    public void remove(SystemDictionary systemDictionary) {

    }

    @Override
    public Optional<SystemDictionary> get(SystemDictionaryKey key) {
        return Optional.empty();
    }
}
