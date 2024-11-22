package cn.ecosync.ibms.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SchedulingTaskParams.ReadDeviceStatusTaskParams.class, name = "ReadDeviceStatus"),
        @JsonSubTypes.Type(value = SchedulingTaskParams.ReadDeviceStatusBatchTaskParams.class, name = "ReadDeviceStatusBatch"),
})
public interface SchedulingTaskParams {
    String type();

    @Getter
    @ToString
    class ReadDeviceStatusTaskParams implements SchedulingTaskParams {
        @NotBlank
        private String deviceCode;

        @Override
        public String type() {
            return "ReadDeviceStatus";
        }
    }

    @Getter
    @ToString
    class ReadDeviceStatusBatchTaskParams implements SchedulingTaskParams {
        @Override
        public String type() {
            return "ReadDeviceStatusBatch";
        }
    }
}
