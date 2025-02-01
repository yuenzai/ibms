package cn.ecosync.ibms.device.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class DeviceSchema {
    @NotBlank
    private String name;
}
