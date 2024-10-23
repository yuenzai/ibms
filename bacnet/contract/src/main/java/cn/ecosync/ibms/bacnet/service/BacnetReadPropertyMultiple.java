package cn.ecosync.ibms.bacnet.service;

import cn.ecosync.ibms.bacnet.model.BacnetObject;
import cn.ecosync.ibms.bacnet.model.BacnetProperty;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultiple {
    @NotNull
    private Integer deviceInstance;
    @Valid
    @NotEmpty
    private Set<BacnetObjectProperties> objectProperties;

    protected BacnetReadPropertyMultiple() {
    }

    public BacnetReadPropertyMultiple(Integer deviceInstance, Map<BacnetObject, Set<BacnetProperty>> objectProperties) {
        this(deviceInstance, objectProperties.entrySet().stream()
                .map(in -> new BacnetObjectProperties(in.getKey(), in.getValue()))
                .collect(Collectors.toSet()));
    }

    public BacnetReadPropertyMultiple(Integer deviceInstance, Set<BacnetObjectProperties> objectProperties) {
        this.deviceInstance = deviceInstance;
        this.objectProperties = objectProperties;
    }

    public Set<BacnetObjectProperties> getObjectProperties() {
        return CollectionUtils.nullSafeOf(objectProperties);
    }

    @Getter
    @ToString
    public static class BacnetObjectProperties extends BacnetObject {
        @Valid
        @NotEmpty
        private Set<BacnetProperty> properties;

        protected BacnetObjectProperties() {
        }

        public BacnetObjectProperties(BacnetObject bacnetObject, Set<BacnetProperty> properties) {
            super(bacnetObject.getObjectType(), bacnetObject.getObjectInstance());
            this.properties = properties;
        }
    }

    public List<String> toCommand() {
        List<String> commands = new ArrayList<>();
        commands.add("readpropm");
        commands.add(String.valueOf(getDeviceInstance()));
        for (BacnetReadPropertyMultiple.BacnetObjectProperties bacnetObject : getObjectProperties()) {
            commands.add(String.valueOf(bacnetObject.getObjectType().getCode()));
            commands.add(bacnetObject.getObjectInstance().toString());
            String propCmdArg = bacnetObject.getProperties().stream()
                    .map(this::commandArgOf)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.joining(","));
            commands.add(propCmdArg);
        }
        return commands;
    }

    public String toCommandString() {
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
