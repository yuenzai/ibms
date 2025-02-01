package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.query.Query;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetDeviceQuery implements Query<Device> {
    @NotBlank
    private String deviceCode;
}
