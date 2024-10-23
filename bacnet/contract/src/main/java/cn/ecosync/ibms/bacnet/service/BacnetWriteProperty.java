package cn.ecosync.ibms.bacnet.service;

import cn.ecosync.ibms.bacnet.model.BacnetObjectProperty;
import cn.ecosync.ibms.bacnet.model.BacnetPropertyValue;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@ToString
public class BacnetWriteProperty {
    @NotNull
    private Integer deviceInstance;
    @Valid
    @NotNull
    @JsonUnwrapped
    private BacnetObjectProperty objectProperty;
    private Integer priority;
    @NotNull
    @JsonUnwrapped
    private BacnetPropertyValue bacnetValue;

    public List<String> toCommand() {
        List<String> commands = new ArrayList<>();

        commands.add("writeprop");
        commands.add(deviceInstance.toString());
        commands.add(objectProperty.getObjectType().getCode().toString());
        commands.add(objectProperty.getObjectInstance().toString());
        commands.add(objectProperty.getPropertyIdentifier().getCode().toString());
        commands.add(getPriority().toString());
        String index = Optional.ofNullable(objectProperty.getPropertyArrayIndex())
                .map(Object::toString)
                .orElse("-1");
        commands.add(index);
        commands.add(bacnetValue.getTag().toString());
        commands.add(bacnetValue.getValue().toString());

        return commands;
    }

    public Integer getPriority() {
        return priority != null ? priority : 0;
    }
}
