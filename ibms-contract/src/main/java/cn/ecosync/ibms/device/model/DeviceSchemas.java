package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetSchemas;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetSchemas.class, name = "BACNET"))
public abstract class DeviceSchemas implements IDeviceSchemas {
    @NotBlank
    private String schemasCode;

    protected DeviceSchemas() {
    }

    protected DeviceSchemas(DeviceSchemasId schemasId) {
        Assert.notNull(schemasId, "schemasId must not be null");
        this.schemasCode = schemasId.getSchemasCode();
    }

    public DeviceSchemasId toSchemasId() {
        return new DeviceSchemasId(schemasCode);
    }

    public abstract DeviceDataAcquisition newDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId);

    public abstract DeviceSchemas toReference();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceSchemas)) return false;
        DeviceSchemas that = (DeviceSchemas) o;
        return Objects.equals(this.schemasCode, that.schemasCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(schemasCode);
    }
}
