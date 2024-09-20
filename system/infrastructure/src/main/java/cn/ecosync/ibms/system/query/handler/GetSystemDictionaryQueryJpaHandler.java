package cn.ecosync.ibms.system.query.handler;

import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.system.model.QSystemDictionary;
import cn.ecosync.ibms.system.query.GetSystemDictionaryQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(JPAQueryFactory.class)
public class GetSystemDictionaryQueryJpaHandler implements QueryHandler<GetSystemDictionaryQuery, Map<String, Object>> {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> handle(GetSystemDictionaryQuery query) {
        QSystemDictionary dict = QSystemDictionary.systemDictionary;
        Map<String, Object> dictValue = jpaQueryFactory.select(dict.value.dictValue)
                .from(dict)
                .where(dict.key.dictKey.eq(query.getDictKey()))
                .fetchOne();
        return dictValue == null ? Collections.emptyMap() : Collections.unmodifiableMap(dictValue);
    }
}
