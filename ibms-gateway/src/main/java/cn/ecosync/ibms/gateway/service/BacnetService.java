package cn.ecosync.ibms.gateway.service;

import cn.ecosync.ibms.JsonSerdeContextHolder;
import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;
import cn.ecosync.ibms.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.bacnet.dto.BacnetPropertyId.PROP_OBJECT_LIST;

@Slf4j
public class BacnetService {
    private final JsonSerde jsonSerde;

    public BacnetService(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    public List<ReadPropertyMultipleAck> readBatch(List<BacnetReadPropertyMultipleService> services) throws Exception {
        String command = services.stream()
                .filter(Objects::nonNull)
                .map(BacnetReadPropertyMultipleService::toCommandString)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining("; echo; "));

        if (!StringUtils.hasText(command)) return Collections.emptyList();

        List<String> commands = Arrays.asList("/bin/bash", "-c", command);
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        log.debug("command: {}\nstdout:\n{}\nstderr:\n{}", commands, stdout, stderr);

        return Arrays.stream(stdout.split("\n"))
                .map(in -> jsonSerde.deserialize(in, new TypeReference<ReadPropertyMultipleAck>() {
                }))
                .filter(ReadPropertyMultipleAck::valuesNotEmpty)
                .collect(Collectors.toList());
    }

    public List<BacnetObject> getDeviceObjectIds(Integer deviceInstance) {
        BacnetObject deviceObject = new BacnetObject(BacnetObjectType.DEVICE, deviceInstance);
        BacnetProperty objectIdsProperty = PROP_OBJECT_LIST.toBacnetProperty();
        BacnetObjectProperties objectProperties = new BacnetObjectProperties(deviceObject, objectIdsProperty);
        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, objectProperties);
        ReadPropertyMultipleAck ack;
        try {
            JsonSerdeContextHolder.set(jsonSerde);
            ack = BacnetReadPropertyMultipleService.execute(service);
            JsonSerdeContextHolder.clear();
            BacnetPropertyResult objectIdsPropertyResult = Optional.ofNullable(ack.toMap())
                    .map(in -> in.get(deviceObject))
                    .map(in -> in.get(objectIdsProperty))
                    .orElse(null);
            if (objectIdsPropertyResult == null) {
                return Collections.emptyList();
            }
            BacnetError bacnetError = objectIdsPropertyResult.getError();
            if (bacnetError != null) {
                throw new RuntimeException(bacnetError.toString());
            }
            return objectIdsPropertyResult.getValues().stream()
                    .filter(in -> in instanceof BacnetPropertyValue.OBJECT_ID)
                    .map(in -> ((BacnetPropertyValue.OBJECT_ID) in).getValue())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }
}
