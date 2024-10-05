package cn.ecosync.ibms.scheduling.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SchedulingTrigger.Cron.class, name = "cron"),
})
public interface SchedulingTrigger {

    @Getter
    @ToString
    class Cron implements SchedulingTrigger {
        private String expression;
    }
}
