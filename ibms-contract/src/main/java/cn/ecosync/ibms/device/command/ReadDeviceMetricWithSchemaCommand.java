package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.dto.DeviceWithSchemaDto;
import cn.ecosync.ibms.device.model.DeviceSchemaId;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class ReadDeviceMetricWithSchemaCommand {
    private ReadDeviceMetricCommand readDeviceCommand;
    private DeviceWithSchemaDto deviceWithSchema;

    protected ReadDeviceMetricWithSchemaCommand() {
    }

    public ReadDeviceMetricWithSchemaCommand(ReadDeviceMetricCommand readDeviceCommand, DeviceWithSchemaDto deviceWithSchema) {
        Assert.notNull(readDeviceCommand, "readDeviceCommand can not be null");
        Assert.notNull(deviceWithSchema, "deviceWithSchema can not be null");
        this.readDeviceCommand = readDeviceCommand;
        this.deviceWithSchema = deviceWithSchema;
    }

    public DeviceSchemaId deviceSchemaId() {
        return deviceWithSchema.getSchema()
                .getDeviceSchemaId();
    }
}
