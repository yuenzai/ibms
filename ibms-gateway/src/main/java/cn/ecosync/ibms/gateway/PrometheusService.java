package cn.ecosync.ibms.gateway;

import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface PrometheusService {
    @PostExchange("/-/reload")
    void reload();
}
