package cn.ecosync.ibms.bacnet.command;

import cn.ecosync.ibms.bacnet.model.BacnetSchemas;
import cn.ecosync.ibms.device.command.SaveDeviceSchemasCommand;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

@ToString
public class SaveBacnetSchemasCommand implements SaveDeviceSchemasCommand {
    @Valid
    @NotNull
    @JsonUnwrapped
    private BacnetSchemas bacnetSchemas;

    @Override
    public BacnetSchemas getSchemas() {
        return bacnetSchemas;
    }
}
