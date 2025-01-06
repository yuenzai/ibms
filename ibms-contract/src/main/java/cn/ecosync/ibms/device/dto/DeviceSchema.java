package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.metrics.InstrumentKindEnum;
import cn.ecosync.ibms.metrics.MeasurementTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class DeviceSchema {
    @NotBlank
    private String name;
    @NotNull
    private InstrumentKindEnum instrumentKind;
    @NotNull
    private MeasurementTypeEnum measurementType;
}
