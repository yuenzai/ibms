package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.metrics.IInstrumentValueType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class DeviceSchema {
    @NotBlank
    private String name;
    private Boolean monotonically;
    private IInstrumentValueType valueType;

    public Boolean getMonotonically() {
        return monotonically != null ? monotonically : Boolean.FALSE;
    }

    public IInstrumentValueType getValueType() {
        return valueType != null ? valueType : IInstrumentValueType.DOUBLE;
    }
}
