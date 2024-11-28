package cn.ecosync.ibms.command;

import cn.ecosync.ibms.model.BacnetWritePropertyService;
import cn.ecosync.iframework.command.Command;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetWritePropertyCommand implements Command {
    @Valid
    @JsonUnwrapped
    private BacnetWritePropertyService service;
}
