package cn.ecosync.ibms.bacnet.query.handler;

import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleBatchQuery;
import cn.ecosync.iframework.query.QueryHandler;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.serde.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BacnetReadPropertyMultipleBatchQueryHandler implements QueryHandler<BacnetReadPropertyMultipleBatchQuery, List<ReadPropertyMultipleAck>> {
    private final JsonSerde jsonSerde;

    @Override
    public List<ReadPropertyMultipleAck> handle(BacnetReadPropertyMultipleBatchQuery query) {
        try {
            List<BacnetReadPropertyMultipleService> services = query.getServices();
            return handleImpl(services);
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }

    private List<ReadPropertyMultipleAck> handleImpl(List<BacnetReadPropertyMultipleService> services) throws Exception {
        String command = services.stream()
                .map(BacnetReadPropertyMultipleService::toCommandString)
                .collect(Collectors.joining("; echo; "));
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
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
