package cn.ecosync.ibms.system.query.handler;

import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.system.query.GetSystemDictionaryQuery;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class GetSystemDictionaryQueryMemoryHandler implements QueryHandler<GetSystemDictionaryQuery, Map<String, Object>> {
    @Override
    public Map<String, Object> handle(GetSystemDictionaryQuery query) {
        return Collections.emptyMap();
    }
}
