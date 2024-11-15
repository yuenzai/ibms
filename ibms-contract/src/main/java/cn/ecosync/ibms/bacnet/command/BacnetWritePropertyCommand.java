package cn.ecosync.ibms.bacnet.command;

import cn.ecosync.ibms.bacnet.model.BacnetWritePropertyService;
import cn.ecosync.ibms.command.Command;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@ToString
public class BacnetWritePropertyCommand implements Command {
    @Valid
    @JsonUnwrapped
    private BacnetWritePropertyService service;
}
