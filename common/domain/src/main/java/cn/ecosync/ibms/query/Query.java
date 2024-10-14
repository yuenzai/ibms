package cn.ecosync.ibms.query;

import cn.ecosync.ibms.util.HttpUrlProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "queryType", visible = true)
public interface Query<R> {
    default HttpUrlProperties httpUrlProperties() {
        return null;
    }
}
