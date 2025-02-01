package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.ibms.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@ToString
public class BacnetWritePropertyService {
    @NotNull
    private Integer deviceInstance;
    @Valid
    @JsonUnwrapped
    private BacnetObject bacnetObject;
    @Valid
    @JsonUnwrapped
    private BacnetProperty bacnetProperty;
    private Integer priority;
    @NotNull
    @JsonUnwrapped
    private BacnetPropertyValue bacnetValue;

    private List<String> toCommand() {
        List<String> commands = new ArrayList<>();
        commands.add("writeprop");
        commands.add(deviceInstance.toString());
        commands.add(bacnetObject.getObjectType().getCode().toString());
        commands.add(bacnetObject.getObjectInstance().toString());
        commands.add(bacnetProperty.getPropertyIdentifier().getCode().toString());
        commands.add(getPriority().toString());
        String index = Optional.ofNullable(bacnetProperty.getPropertyArrayIndex())
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

    public static void execute(BacnetWritePropertyService service) throws Exception {
        List<String> command = service.toCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        log.info("Execute command[{}]", String.join(" ", command));
        if (StringUtils.hasText(stdout)) log.info("{}", stdout);
        if (StringUtils.hasText(stderr)) throw new RuntimeException(stderr);
    }
}
