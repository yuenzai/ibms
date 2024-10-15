package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.model.BacnetDeviceExtra;
import cn.ecosync.ibms.bacnet.model.BacnetDevicePointExtra;
import cn.ecosync.ibms.bacnet.model.BacnetObject;
import cn.ecosync.ibms.bacnet.model.BacnetProperty;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.HttpRequestProperties;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultipleToFileQuery implements Query<Void> {
    @NotNull
    private Integer deviceInstance;
    @NotEmpty
    private List<BacnetObjectProperties> objectProperties;

    protected BacnetReadPropertyMultipleToFileQuery() {
    }

    public BacnetReadPropertyMultipleToFileQuery(Integer deviceInstance, Map<BacnetObject, List<BacnetProperty>> objectProperties) {
        this(deviceInstance, objectProperties.entrySet().stream()
                .map(in -> new BacnetObjectProperties(in.getKey(), in.getValue()))
                .collect(Collectors.toList()));
    }

    public BacnetReadPropertyMultipleToFileQuery(Integer deviceInstance, List<BacnetObjectProperties> objectProperties) {
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
        for (BacnetReadPropertyMultipleToFileQuery.BacnetObjectProperties bacnetObject : getObjectProperties()) {
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

    @Override
    public HttpRequestProperties httpRequestProperties() {
        return HttpRequestProperties.builder()
                .hostEnvironmentKey(BacnetConstants.ENV_BACNET_SERVICE_HOST)
                .pathSegments(BacnetConstants.BACNET, "readpropmToFile")
                .header("args", String.join(" ", this.commandArgs()))
                .build();
    }

    public static Optional<BacnetReadPropertyMultipleToFileQuery> newInstance(DeviceDto device) {
        if (device == null) {
            return Optional.empty();
        }
        // 设备配置属性
        BacnetDeviceExtra deviceProperties = (BacnetDeviceExtra) device.getDeviceExtra();
        // 点位配置属性
        List<BacnetDevicePointExtra> devicePoints = device.getDevicePoints().stream()
                .filter(in -> in.getPointExtra() instanceof BacnetDevicePointExtra)
                .map(in -> (BacnetDevicePointExtra) in.getPointExtra())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(devicePoints)) {
            return Optional.empty();
        }

        Map<BacnetObject, List<BacnetProperty>> objectProperties = devicePoints.stream()
                .collect(Collectors.groupingBy(BacnetDevicePointExtra::toBacnetObject, Collectors.mapping(BacnetDevicePointExtra::toBacnetProperty, Collectors.toList())));

        return Optional.of(new BacnetReadPropertyMultipleToFileQuery(deviceProperties.getDeviceInstance(), objectProperties));
    }
}
