package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultipleService {
    private Integer deviceInstance;
    private Collection<BacnetObjectProperties> objectPropertiesCollection;

    protected BacnetReadPropertyMultipleService() {
    }

    public BacnetReadPropertyMultipleService(Integer deviceInstance, Collection<BacnetObjectProperties> objectPropertiesCollection) {
        Assert.notNull(deviceInstance, "deviceInstance must not be null");
        Assert.notEmpty(objectPropertiesCollection, "objectPropertiesCollection must not be empty");
        this.deviceInstance = deviceInstance;
        this.objectPropertiesCollection = objectPropertiesCollection;
    }

    public List<String> toCommand() {
        if (CollectionUtils.isEmpty(objectPropertiesCollection)) return Collections.emptyList();

        List<String> commands = new ArrayList<>();
        commands.add("readpropm");
        commands.add(String.valueOf(getDeviceInstance()));

        for (BacnetObjectProperties bacnetObject : objectPropertiesCollection) {
            commands.add(String.valueOf(bacnetObject.getBacnetObject().getObjectType().getCode()));
            commands.add(bacnetObject.getBacnetObject().getObjectInstance().toString());
            String propCmdArg = bacnetObject.toProperties().stream()
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
