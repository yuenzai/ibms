package cn.ecosync.ibms.system.query.handler;

import cn.ecosync.ibms.system.model.DictionaryValue;
import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.system.model.Dictionary;
import cn.ecosync.ibms.system.query.GetDictionaryQuery;
import cn.ecosync.ibms.system.repository.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(JpaRepository.class)
public class GetDictionaryQueryJpaHandler implements QueryHandler<GetDictionaryQuery, Optional<DictionaryValue>> {
    private final DictionaryRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<DictionaryValue> handle(GetDictionaryQuery query) {
        return repository.get(query.getDictKey())
                .map(Dictionary::getValue);
    }
}
