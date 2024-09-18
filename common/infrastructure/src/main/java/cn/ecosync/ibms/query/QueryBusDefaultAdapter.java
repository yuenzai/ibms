package cn.ecosync.ibms.query;

import cn.ecosync.ibms.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 覃俊元
 * @since 2024
 */
@Slf4j
@Component
public class QueryBusDefaultAdapter implements QueryBus {
    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers;

    public QueryBusDefaultAdapter(List<QueryHandler<?, ?>> queryHandlers) {
        this.queryHandlers = queryHandlers.stream()
                .map(in -> new AbstractMap.SimpleImmutableEntry<>(getQueryType(in), in))
                .filter(in -> in.getKey() != null)
                .peek(in -> log.info("register query handler[{}]", CollectionUtils.mapOf("queryType", in.getKey().getCanonicalName(), "queryHandler", in.getValue().getClass().getCanonicalName())))
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, Map.Entry::getValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Query<R>, R> R execute(T query) {
        Assert.notNull(query, "query is null");
        QueryHandler<?, ?> queryHandler = queryHandlers.get(query.getClass());
        Assert.notNull(queryHandler, "query handler not found: " + query.getClass().getCanonicalName());
        Method handleMethod = ReflectionUtils.findMethod(queryHandler.getClass(), "handle", query.getClass());
        Assert.notNull(handleMethod, "handle method not found: " + queryHandler.getClass().getCanonicalName());
        log.debug("execute query[{}]", CollectionUtils.mapOf("queryType", query.getClass().getCanonicalName(), "queryHandler", queryHandler.getClass().getCanonicalName()));
        return (R) ReflectionUtils.invokeMethod(handleMethod, queryHandler, query);
    }

    private Class<?> getQueryType(QueryHandler<?, ?> queryHandler) {
        return ResolvableType.forClass(QueryHandler.class, queryHandler.getClass())
                .resolveGeneric(0);
    }
}
