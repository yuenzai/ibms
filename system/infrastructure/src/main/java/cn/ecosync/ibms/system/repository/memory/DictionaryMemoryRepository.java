package cn.ecosync.ibms.system.repository.memory;

import cn.ecosync.ibms.system.model.Dictionary;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.repository.DictionaryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class DictionaryMemoryRepository implements DictionaryRepository {
    @Override
    public void put(Dictionary dictionary) {

    }

    @Override
    public void remove(Dictionary dictionary) {

    }

    @Override
    public Optional<Dictionary> get(DictionaryKey key) {
        return Optional.empty();
    }
}
