package cn.ecosync.ibms.bacnet.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@ToString
public class BacnetWritePropertyService {
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
        commands.add(objectProperty.getBacnetObject().getObjectType().getCode().toString());
        commands.add(objectProperty.getBacnetObject().getObjectInstance().toString());
        commands.add(objectProperty.getBacnetProperty().getPropertyIdentifier().getCode().toString());
        commands.add(getPriority().toString());
        String index = Optional.ofNullable(objectProperty.getBacnetProperty().getPropertyArrayIndex())
                .map(Object::toString)
                .orElse("-1");
        commands.add(index);
        commands.add(bacnetValue.getValueType());
        commands.add(bacnetValue.getValue().toString());

        return commands;
    }

    public Integer getPriority() {
        return priority != null ? priority : 0;
    }
}
