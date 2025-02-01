package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.util.CollectionUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.ToString;

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

    public BacnetSchemas(DeviceSchemasId schemasId, List<BacnetSchema> schemas) {
        super(schemasId);
        this.schemas = schemas;
    }

    @Override
    public List<BacnetSchema> getSchemas() {
        return CollectionUtils.nullSafeOf(schemas);
    }

    @Override
    @AssertTrue(message = "Schema name duplicated")
    public boolean checkUniqueName() {
        return super.checkUniqueName();
    }

    @Override
    public BacnetSchemas toReference() {
        return new BacnetSchemas(getSchemasId());
    }
}
