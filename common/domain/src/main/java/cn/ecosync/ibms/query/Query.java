package cn.ecosync.ibms.query;

import cn.ecosync.ibms.util.HttpRequestProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "queryType", visible = true)
public interface Query<R> {
    default HttpRequestProperties httpRequestProperties() {
        return null;
    }
}
