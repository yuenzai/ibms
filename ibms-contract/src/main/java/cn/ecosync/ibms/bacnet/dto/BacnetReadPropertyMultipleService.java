package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultipleService {
    @NotNull
    private Integer deviceInstance;
    @NotEmpty
    private Collection<@Valid BacnetObjectProperties> bacnetDataPoints;

    protected BacnetReadPropertyMultipleService() {
    }

    public BacnetReadPropertyMultipleService(Integer deviceInstance, BacnetObjectProperties... bacnetDataPoints) {
        this(deviceInstance, Arrays.asList(bacnetDataPoints));
    }

    public BacnetReadPropertyMultipleService(Integer deviceInstance, Collection<BacnetObjectProperties> bacnetDataPoints) {
        Assert.notNull(deviceInstance, "deviceInstance must not be null");
        Assert.notEmpty(bacnetDataPoints, "bacnetDataPoints must not be null");
        this.deviceInstance = deviceInstance;
        this.bacnetDataPoints = bacnetDataPoints;
    }

    public Collection<BacnetObjectProperties> getBacnetDataPoints() {
        return Collections.unmodifiableCollection(bacnetDataPoints);
    }

    public List<String> toCommand() {
        List<String> commands = new ArrayList<>();
        commands.add("readpropm");
        commands.add(String.valueOf(getDeviceInstance()));

        for (BacnetObjectProperties dataPoint : bacnetDataPoints) {
            commands.add(String.valueOf(dataPoint.getBacnetObject().getObjectType().getCode()));
            commands.add(dataPoint.getBacnetObject().getObjectInstance().toString());
            String propCmdArg = dataPoint.getProperties().stream()
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
