package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultipleService {
    @NotNull
    private Integer deviceInstance;
    @Valid
    @NotEmpty
    private Set<BacnetObjectProperties> objectProperties;

    protected BacnetReadPropertyMultipleService() {
    }

    public BacnetReadPropertyMultipleService(Integer deviceInstance, Set<BacnetObjectProperties> objectProperties) {
        this.deviceInstance = deviceInstance;
        this.objectProperties = objectProperties;
    }

    public Set<BacnetObjectProperties> getObjectProperties() {
        return CollectionUtils.nullSafeOf(objectProperties);
    }

    public static <T> BacnetReadPropertyMultipleService newInstance(Integer deviceInstance, Collection<T> collection, Function<T, BacnetObjectProperty> function) {
        Set<BacnetObjectProperties> objectProperties = BacnetObjectProperties.newInstance(collection, function);
        return new BacnetReadPropertyMultipleService(deviceInstance, objectProperties);
    }

    @Getter
    @ToString
    public static class BacnetObjectProperties {
        @Valid
        @JsonUnwrapped
        private BacnetObject bacnetObject;
        @Valid
        @NotEmpty
        private Set<BacnetProperty> properties;

        protected BacnetObjectProperties() {
        }

        public BacnetObjectProperties(BacnetObject bacnetObject, Set<BacnetProperty> properties) {
            this.bacnetObject = bacnetObject;
            this.properties = properties;
        }

        public static <T> Set<BacnetObjectProperties> newInstance(Collection<T> collection, Function<T, BacnetObjectProperty> function) {
            if (CollectionUtils.isEmpty(collection)) return Collections.emptySet();
            Map<BacnetObject, Set<BacnetProperty>> map = new HashMap<>();
            for (T element : collection) {
                BacnetObjectProperty bop = function.apply(element);
                BacnetObject key = bop.getBacnetObject();
                Set<BacnetProperty> properties = map.computeIfAbsent(key, in -> new HashSet<>());
                properties.add(bop.getBacnetProperty());
            }
            return map.entrySet().stream()
                    .map(in -> new BacnetObjectProperties(in.getKey(), in.getValue()))
                    .collect(Collectors.toSet());
        }
    }

    public List<String> toCommand() {
        if (CollectionUtils.isEmpty(getObjectProperties())) return Collections.emptyList();

        List<String> commands = new ArrayList<>();
        commands.add("readpropm");
        commands.add(String.valueOf(getDeviceInstance()));

        for (BacnetObjectProperties bacnetObject : getObjectProperties()) {
            commands.add(String.valueOf(bacnetObject.bacnetObject.getObjectType().getCode()));
            commands.add(bacnetObject.bacnetObject.getObjectInstance().toString());
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
