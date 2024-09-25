package cn.ecosync.ibms.device;

import cn.ecosync.ibms.device.model.bacnet.BacnetProperty;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import cn.ecosync.ibms.device.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BacnetApplicationService {
    private final JsonSerde jsonSerde;

    public List<ReadPropertyMultipleAck> handle(BacnetReadPropertyMultipleQuery query) throws IOException, InterruptedException {
        List<String> commands = new ArrayList<>();
        commands.add("readpropm");
        commands.add(String.valueOf(query.getDeviceInstance()));
        for (BacnetReadPropertyMultipleQuery.BacnetObjectProperties bacnetObject : query.getObjectProperties()) {
            commands.add(String.valueOf(bacnetObject.getObjectType().getCode()));
            commands.add(bacnetObject.getObjectInstance().toString());
            String propCmdArg = bacnetObject.getProperties().stream()
                    .map(this::commandArgOf)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.joining(","));
            commands.add(propCmdArg);
        }
        log.info("Execute command: {}", String.join(" ", commands));
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            return jsonSerde.readValue(stdout, new TypeReference<List<ReadPropertyMultipleAck>>() {
            }).orElse(Collections.emptyList());
        } else {
            throw new RuntimeException("Bacnet: ReadPropertyMultiple error: " + stdout);
        }
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
