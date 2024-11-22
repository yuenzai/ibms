package cn.ecosync.ibms.scheduling.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SchedulingTrigger.Cron.class, name = "CRON"),
        @JsonSubTypes.Type(value = SchedulingTrigger.None.class, name = "NONE"),
})
public interface SchedulingTrigger {
    @Getter
    @ToString
    class Cron implements SchedulingTrigger {
        @NotBlank
        private String expression;
    }

    @ToString
    class None implements SchedulingTrigger {
        public static final None INSTANCE = new None();
    }
}
