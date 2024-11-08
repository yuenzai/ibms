package cn.ecosync.ibms.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * http日志拦截器
 *
 * @author 覃俊元
 * @since 2024
 */
@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        log.info("[httpRequest={}]", toString(request));
        ClientHttpResponse response = execution.execute(request, body);
        log.info("[httpResponse={}]", toString(response));
        return response;
    }

    public Object toString(HttpRequest request) {
        Map<String, Object> map = CollectionUtils.newHashMap(3);
        map.put("methodValue", request.getMethodValue());
        map.put("uri", request.getURI());
        map.put("headers", request.getHeaders());
        return map;
    }

    public Object toString(ClientHttpResponse response) throws IOException {
        Map<String, Object> map = CollectionUtils.newHashMap(4);
        map.put("statusCode", response.getRawStatusCode());
        map.put("statusText", response.getStatusText());
        map.put("headers", response.getHeaders());
        map.put("body", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        return map;
    }
}
