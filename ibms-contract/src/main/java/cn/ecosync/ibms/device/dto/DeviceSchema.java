package cn.ecosync.ibms.device.dto;

import io.opentelemetry.sdk.metrics.InstrumentValueType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class DeviceSchema {
    @NotBlank
    private String name;
    private Boolean monotonically;
    private InstrumentValueType valueType;

    public Boolean getMonotonically() {
        return monotonically != null ? monotonically : Boolean.FALSE;
    }

    public InstrumentValueType getValueType() {
        return valueType != null ? valueType : InstrumentValueType.DOUBLE;
    }
}
