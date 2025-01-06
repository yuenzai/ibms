package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.iframework.query.Query;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetSchemasQuery implements Query<DeviceSchemas> {
    @NotBlank
    private String schemasCode;
}
