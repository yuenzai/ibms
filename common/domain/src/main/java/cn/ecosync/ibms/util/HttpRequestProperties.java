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
public class HttpRequestProperties {
    private final String hostEnvironmentKey;
    private final String[] pathSegments;
    private final MultiValueMap<String, String> queryParams;
    private final MultiValueMap<String, String> headers;

    private HttpRequestProperties(String hostEnvironmentKey, String[] pathSegments, MultiValueMap<String, String> queryParams, MultiValueMap<String, String> headers) {
        this.hostEnvironmentKey = hostEnvironmentKey;
        this.pathSegments = pathSegments;
        this.queryParams = queryParams;
        this.headers = headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String hostEnvironmentKey;
        private String[] pathSegments;
        private final MultiValueMap<String, String> queryParams;
        private final MultiValueMap<String, String> headers;

        private Builder() {
            this.queryParams = new LinkedMultiValueMap<>();
            this.headers = new LinkedMultiValueMap<>();
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

        public Builder header(String name, String value) {
            this.headers.add(name, value);
            return this;
        }

        public HttpRequestProperties build() {
            return new HttpRequestProperties(this.hostEnvironmentKey, this.pathSegments, this.queryParams, this.headers);
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
