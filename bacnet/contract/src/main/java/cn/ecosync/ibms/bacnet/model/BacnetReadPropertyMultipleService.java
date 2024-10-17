package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.DeviceDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultipleService {
    @NotNull
    private Integer deviceInstance;
    @NotEmpty
    private List<BacnetObjectProperties> objectProperties;

    protected BacnetReadPropertyMultipleService() {
    }

    public BacnetReadPropertyMultipleService(Integer deviceInstance, Map<BacnetObject, List<BacnetProperty>> objectProperties) {
        this(deviceInstance, objectProperties.entrySet().stream()
                .map(in -> new BacnetObjectProperties(in.getKey(), in.getValue()))
                .collect(Collectors.toList()));
    }

    public BacnetReadPropertyMultipleService(Integer deviceInstance, List<BacnetObjectProperties> objectProperties) {
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

    public List<String> toCommand() {
        List<String> commands = new ArrayList<>();
        commands.add("readpropm");
        commands.add(String.valueOf(getDeviceInstance()));
        for (BacnetReadPropertyMultipleService.BacnetObjectProperties bacnetObject : getObjectProperties()) {
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

    public static BacnetReadPropertyMultipleService newInstance(DeviceDto device) {
        Assert.notNull(device, "deviceDto must not be null");
        // 设备配置属性
        BacnetDeviceExtra deviceProperties = (BacnetDeviceExtra) device.getDeviceExtra();
        // 点位配置属性
        List<BacnetDevicePointExtra> devicePoints = device.getDevicePoints().stream()
                .filter(in -> in.getPointExtra() instanceof BacnetDevicePointExtra)
                .map(in -> (BacnetDevicePointExtra) in.getPointExtra())
                .collect(Collectors.toList());
        Assert.notEmpty(devicePoints, "devicePoints must not be empty");
        Map<BacnetObject, List<BacnetProperty>> objectProperties = devicePoints.stream()
                .collect(Collectors.groupingBy(BacnetDevicePointExtra::toBacnetObject, Collectors.mapping(BacnetDevicePointExtra::toBacnetProperty, Collectors.toList())));
        return new BacnetReadPropertyMultipleService(deviceProperties.getDeviceInstance(), objectProperties);
    }
}
