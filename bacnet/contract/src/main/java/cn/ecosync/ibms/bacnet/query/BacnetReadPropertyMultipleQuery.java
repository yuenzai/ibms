package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.model.BacnetObject;
import cn.ecosync.ibms.bacnet.model.BacnetProperty;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultipleQuery {
    @NotNull
    private Integer deviceInstance;
    @NotEmpty
    private List<BacnetObjectProperties> objectProperties;

    protected BacnetReadPropertyMultipleQuery() {
    }

    public BacnetReadPropertyMultipleQuery(Integer deviceInstance, Map<BacnetObject, List<BacnetProperty>> objectProperties) {
        this(deviceInstance, objectProperties.entrySet().stream()
                .map(in -> new BacnetObjectProperties(in.getKey(), in.getValue()))
                .collect(Collectors.toList()));
    }

    public BacnetReadPropertyMultipleQuery(Integer deviceInstance, List<BacnetObjectProperties> objectProperties) {
        this.deviceInstance = deviceInstance;
        this.objectProperties = objectProperties;
    }

    @Getter
    @ToString
    public static class BacnetObjectProperties extends BacnetObject {
        @NotEmpty
        private List<BacnetProperty> properties;

        protected BacnetObjectProperties() {
        }

        public BacnetObjectProperties(BacnetObject bacnetObject, List<BacnetProperty> properties) {
            super(bacnetObject.getObjectType(), bacnetObject.getObjectInstance());
            this.properties = properties;
        }
    }

    public List<String> commandArgs() {
        List<String> commands = new ArrayList<>();
        commands.add(String.valueOf(getDeviceInstance()));
        for (BacnetReadPropertyMultipleQuery.BacnetObjectProperties bacnetObject : getObjectProperties()) {
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

    private String commandArgOf(BacnetProperty property) {
        String prop = String.valueOf(property.getPropertyIdentifier().getCode());
        Integer index = property.getPropertyArrayIndex().orElse(null);
        if (index != null) {
            prop += "[" + index + "]";
        }
        return prop;
    }
}
