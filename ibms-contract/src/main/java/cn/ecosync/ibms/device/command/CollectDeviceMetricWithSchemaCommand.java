package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.dto.DeviceWithSchemaDto;
import cn.ecosync.ibms.device.model.DeviceSchemaId;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class CollectDeviceMetricWithSchemaCommand {
    private CollectDeviceMetricCommand readDeviceCommand;
    private DeviceWithSchemaDto deviceWithSchema;

    protected CollectDeviceMetricWithSchemaCommand() {
    }

    public CollectDeviceMetricWithSchemaCommand(CollectDeviceMetricCommand collectDeviceCommand, DeviceWithSchemaDto deviceWithSchema) {
        Assert.notNull(collectDeviceCommand, "collectDeviceCommand can not be null");
        Assert.notNull(deviceWithSchema, "deviceWithSchema can not be null");
        this.readDeviceCommand = collectDeviceCommand;
        this.deviceWithSchema = deviceWithSchema;
    }

    public DeviceSchemaId deviceSchemaId() {
        return deviceWithSchema.getSchema()
                .getDeviceSchemaId();
    }
}
