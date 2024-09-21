package cn.ecosync.ibms.system.query.handler;

import cn.ecosync.ibms.system.model.DictionaryValue;
import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.system.query.GetDictionaryQuery;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class GetDictionaryQueryMemoryHandler implements QueryHandler<GetDictionaryQuery, Optional<DictionaryValue>> {
    @Override
    public Optional<DictionaryValue> handle(GetDictionaryQuery query) {
        return Optional.empty();
    }
}
