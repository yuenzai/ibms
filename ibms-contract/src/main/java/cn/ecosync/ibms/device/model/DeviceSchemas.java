package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetSchemas;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetSchemas.class, name = "BACNET"))
public abstract class DeviceSchemas implements IDeviceSchemas {
    @Valid
    @NotNull
    @JsonUnwrapped
    private DeviceSchemasId schemasId;

    protected DeviceSchemas() {
    }

    protected DeviceSchemas(DeviceSchemasId schemasId) {
        Assert.notNull(schemasId, "schemasId must not be null");
        this.schemasId = schemasId;
    }

    public abstract DeviceDataAcquisition newDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval);

    public abstract DeviceSchemas toReference();
}
