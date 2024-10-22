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
    private BacnetPropertyValue value;

    public List<String> toCommand() {
        List<String> commands = new ArrayList<>();
        commands.add("writeprop");
        commands.add(String.valueOf(getDeviceInstance()));
        commands.add(String.valueOf(objectProperty.getObjectType().getCode()));
        commands.add(objectProperty.getObjectInstance().toString());

        String prop = String.valueOf(objectProperty.getPropertyIdentifier().getCode());
        commands.add(prop);

        commands.add(String.valueOf(getPriority()));

        String index = Optional.ofNullable(objectProperty.getPropertyArrayIndex())
                .map(String::valueOf)
                .orElse("-1");
        commands.add(index);

        //todo add tag and value to command
        return commands;
    }

    public Integer getPriority() {
        return priority != null ? priority : 16;
    }
}
