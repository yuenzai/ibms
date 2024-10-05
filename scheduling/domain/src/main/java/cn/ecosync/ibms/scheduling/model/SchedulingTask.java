package cn.ecosync.ibms.scheduling.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = SchedulingTask.Http.class, name = "http"),
//})
public interface SchedulingTask extends Runnable {
//    class Http implements SchedulingTask {
//    }
}
