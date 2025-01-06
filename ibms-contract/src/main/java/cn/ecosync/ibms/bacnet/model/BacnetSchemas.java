package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
public class BacnetSchemas extends DeviceSchemas {
    @Valid
    private List<BacnetSchema> schemas;

    protected BacnetSchemas() {
    }

    protected BacnetSchemas(DeviceSchemasId schemasId) {
        super(schemasId);
    }

    @Override
    public BacnetDataAcquisition newDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId) {
        return new BacnetDataAcquisition(dataAcquisitionId, toReference(), Collections.emptyList());
    }

    public BacnetSchemas(DeviceSchemasId schemasId, List<BacnetSchema> schemas) {
        super(schemasId);
        this.schemas = schemas;
    }

    @Override
    public List<BacnetSchema> getSchemaItems() {
        return CollectionUtils.nullSafeOf(schemas);
    }

    @Override
    @JsonIgnore
    @AssertTrue(message = "Schema name duplicated")
    public boolean isUniqueName() {
        return super.isUniqueName();
    }

    @Override
    public BacnetSchemas toReference() {
        return new BacnetSchemas(getSchemasId());
    }
}
