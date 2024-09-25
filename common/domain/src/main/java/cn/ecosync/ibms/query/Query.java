package cn.ecosync.ibms.query;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "queryType", visible = true)
public interface Query<R> {
}
