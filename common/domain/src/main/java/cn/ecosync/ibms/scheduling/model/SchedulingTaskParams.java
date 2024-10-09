package cn.ecosync.ibms.scheduling.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
public interface SchedulingTaskParams {
    String type();

    Map<String, Object> toParams();
}
