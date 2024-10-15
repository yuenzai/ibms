package cn.ecosync.ibms.query;

import cn.ecosync.ibms.util.HttpRequest;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "queryType", visible = true)
public interface Query<R> {
    default HttpRequest httpRequest() {
        return null;
    }
}
