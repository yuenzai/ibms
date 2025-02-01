package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.ibms.JsonSerdeContextHolder;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.StringUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@ToString
public class BacnetReadPropertyMultipleService {
    private static final String SEGMENTATION_NOT_SUPPORTED = "BACnet Abort: Segmentation Not Supported\n";

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

    public static ReadPropertyMultipleAck execute(BacnetReadPropertyMultipleService service) throws SegmentationNotSupportedException, IOException, InterruptedException {
        List<String> command = service.toCommand();
        log.info("Execute command[{}]", String.join(" ", command));
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        if (StringUtils.hasText(stdout)) log.info("{}", stdout);
        if (SEGMENTATION_NOT_SUPPORTED.equals(stderr)) throw new SegmentationNotSupportedException();
        if (StringUtils.hasText(stderr)) throw new RuntimeException(stderr);
        JsonSerde jsonSerde = JsonSerdeContextHolder.get();
        return jsonSerde.deserialize(stdout, new TypeReference<ReadPropertyMultipleAck>() {
        });
    }

    public static class SegmentationNotSupportedException extends RuntimeException {
    }
}
