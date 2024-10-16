package cn.ecosync.ibms.query;

import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Method;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
    private final RestTemplate restTemplate;
    private final Environment environment;

    public QueryBusDefaultAdapter(List<QueryHandler<?, ?>> queryHandlers, RestTemplateBuilder builder, Environment environment) {
        this.queryHandlers = queryHandlers.stream()
                .map(in -> new AbstractMap.SimpleImmutableEntry<>(getQueryType(in), in))
                .filter(in -> in.getKey() != null)
                .peek(in -> log.info("register query handler[{}]", CollectionUtils.mapOf("queryType", in.getKey().getCanonicalName(), "queryHandler", in.getValue().getClass().getCanonicalName())))
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, Map.Entry::getValue));
        this.restTemplate = builder.setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        this.environment = environment;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Query<R>, R> R execute(T query) {
        Assert.notNull(query, "query is null");
        QueryHandler<?, ?> queryHandler = queryHandlers.get(query.getClass());
        // Assert.notNull(queryHandler, "query handler not found: " + query.getClass().getCanonicalName());
        if (queryHandler == null) {
            return remoteExecute(query);
        }
        Method handleMethod = ReflectionUtils.findMethod(queryHandler.getClass(), "handle", query.getClass());
        Assert.notNull(handleMethod, "handle method not found: " + queryHandler.getClass().getCanonicalName());
        log.debug("execute query[{}]", CollectionUtils.mapOf("queryType", query.getClass().getCanonicalName(), "queryHandler", queryHandler.getClass().getCanonicalName()));
        return (R) ReflectionUtils.invokeMethod(handleMethod, queryHandler, query);
    }

    private Class<?> getQueryType(QueryHandler<?, ?> queryHandler) {
        return ResolvableType.forClass(QueryHandler.class, queryHandler.getClass())
                .resolveGeneric(0);
    }

    @SuppressWarnings("unchecked")
    private <T extends Query<R>, R> R remoteExecute(T query) {
        HttpRequest httpUrl = query.httpRequest();
        Assert.notNull(httpUrl, "query must have queryHandler or httpUrl");

        HttpMethod httpMethod = HttpMethod.resolve(httpUrl.getHttpMethod());
        Assert.notNull(httpMethod, "unknown http method: " + httpUrl.getHttpMethod());

        String host = environment.getProperty(httpUrl.getHostEnvironmentKey());
        Assert.hasText(host, "environment required: " + httpUrl.getHostEnvironmentKey());

        URI uri = UriComponentsBuilder.fromHttpUrl("http://" + host)
                .pathSegment(httpUrl.getPathSegments())
                .queryParams(httpUrl.getQueryParams())
                .build()
                .toUri();

        HttpEntity<?> httpEntity = HttpMethod.POST == httpMethod && httpUrl.getRequestBody() != null ?
                new HttpEntity<>(httpUrl.getRequestBody()) : RequestEntity.EMPTY;

        ResolvableType queryReturnType = getQueryReturnType(query);
        if (queryReturnType.hasGenerics()) {
            return (R) this.restTemplate.exchange(uri, httpMethod, httpEntity, ParameterizedTypeReference.forType(queryReturnType.resolve())).getBody();
        } else {
            return (R) this.restTemplate.exchange(uri, httpMethod, httpEntity, queryReturnType.resolve()).getBody();
        }
    }

    private ResolvableType getQueryReturnType(Query<?> query) {
        return ResolvableType.forClass(Query.class, query.getClass())
                .getGeneric(0);
    }
}
