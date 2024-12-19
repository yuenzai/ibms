package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultipleService {
    @NotNull
    private Integer deviceInstance;
    @Valid
    @NotEmpty
    private Collection<BacnetObjectProperties> bopss;

    protected BacnetReadPropertyMultipleService() {
    }

    public BacnetReadPropertyMultipleService(Integer deviceInstance, Collection<? extends BacnetObjectProperties> bopss) {
        this.deviceInstance = deviceInstance;
        this.bopss = new HashSet<>(bopss);
    }

    public Collection<BacnetObjectProperties> getBopss() {
        return CollectionUtils.nullSafeOf(bopss);
    }

    public List<String> toCommand() {
        if (CollectionUtils.isEmpty(getBopss())) return Collections.emptyList();

        List<String> commands = new ArrayList<>();
        commands.add("readpropm");
        commands.add(String.valueOf(getDeviceInstance()));

        for (BacnetObjectProperties bacnetObject : getBopss()) {
            commands.add(String.valueOf(bacnetObject.getBacnetObject().getObjectType().getCode()));
            commands.add(bacnetObject.getBacnetObject().getObjectInstance().toString());
            String propCmdArg = bacnetObject.getProperties().stream()
                    .map(this::commandArgOf)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.joining(","));
            commands.add(propCmdArg);
        }
        return commands;
    }

    public String toCommandString() {
        if (CollectionUtils.isEmpty(toCommand())) return "";
        return String.join(" ", toCommand());
    }

    private String commandArgOf(BacnetProperty property) {
        String prop = String.valueOf(property.getPropertyIdentifier().getCode());
        Integer index = property.getPropertyArrayIndex().orElse(null);
        if (index != null) {
            prop += "[" + index + "]";
        }
        return prop;
    }
}
