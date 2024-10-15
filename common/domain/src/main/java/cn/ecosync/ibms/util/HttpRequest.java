package cn.ecosync.ibms.util;

import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Getter
@ToString
public class HttpRequest {
    private final String httpMethod;
    private final String hostEnvironmentKey;
    private final String[] pathSegments;
    private final MultiValueMap<String, String> queryParams;
    private final Object requestBody;

    private HttpRequest(String httpMethod, String hostEnvironmentKey, String[] pathSegments, MultiValueMap<String, String> queryParams, Object requestBody) {
        this.httpMethod = httpMethod;
        this.hostEnvironmentKey = hostEnvironmentKey;
        this.pathSegments = pathSegments;
        this.queryParams = queryParams;
        this.requestBody = requestBody;
    }

    public static Builder getMethod() {
        return new Builder("GET");
    }

    public static Builder postMethod() {
        return new Builder("POST");
    }

    public static class Builder {
        private final String httpMethod;
        private String hostEnvironmentKey;
        private String[] pathSegments;
        private final MultiValueMap<String, String> queryParams;
        private Object requestBody;

        private Builder(String httpMethod) {
            this.httpMethod = httpMethod;
            this.queryParams = new LinkedMultiValueMap<>();
        }

        public Builder hostEnvironmentKey(String hostEnvironmentKey) {
            this.hostEnvironmentKey = hostEnvironmentKey;
            return this;
        }

        public Builder pathSegments(String... pathSegments) {
            this.pathSegments = pathSegments;
            return this;
        }

        public Builder queryParam(String name, Object... values) {
            Assert.notNull(name, "Name must not be null");
            if (!ObjectUtils.isEmpty(values)) {
                for (Object value : values) {
                    String valueAsString = getQueryParamValue(value);
                    this.queryParams.add(name, valueAsString);
                }
            } else {
                this.queryParams.add(name, null);
            }
            return this;
        }

        public Builder queryParams(MultiValueMap<String, String> queryParams) {
            this.queryParams.putAll(queryParams);
            return this;
        }

        public Builder requestBody(Object requestBody) {
            if ("GET".equals(this.httpMethod)) {
                throw new UnsupportedOperationException("get method should not pass a request body");
            }
            this.requestBody = requestBody;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this.httpMethod, this.hostEnvironmentKey, this.pathSegments, this.queryParams, this.requestBody);
        }

        @Nullable
        private String getQueryParamValue(@Nullable Object value) {
            if (value != null) {
                return (value instanceof Optional ?
                        ((Optional<?>) value).map(Object::toString).orElse(null) :
                        value.toString());
            }
            return null;
        }
    }
}
