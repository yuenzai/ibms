package cn.ecosync.ibms.device.dto;


import cn.ecosync.ibms.device.model.DeviceQueryModel;
import cn.ecosync.ibms.device.model.DeviceSchemaQueryModel;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class DeviceWithSchemaDto {
    private DeviceQueryModel device;
    private DeviceSchemaQueryModel schema;

    protected DeviceWithSchemaDto() {
    }

    public DeviceWithSchemaDto(DeviceQueryModel device, DeviceSchemaQueryModel schema) {
        Assert.notNull(device, "device must not be null");
        Assert.notNull(schema, "schema must not be null");
        this.device = device;
        this.schema = schema;
    }
}
