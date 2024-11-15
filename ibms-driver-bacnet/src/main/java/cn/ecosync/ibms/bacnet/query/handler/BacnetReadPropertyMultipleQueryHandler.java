package cn.ecosync.ibms.bacnet.query.handler;

import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;
import cn.ecosync.ibms.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BacnetReadPropertyMultipleQueryHandler implements QueryHandler<BacnetReadPropertyMultipleQuery, ReadPropertyMultipleAck> {
    private final JsonSerde jsonSerde;

    @Override
    public ReadPropertyMultipleAck handle(BacnetReadPropertyMultipleQuery query) {
        try {
            BacnetReadPropertyMultipleService service = query.getService();
            return handleImpl(service);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    private ReadPropertyMultipleAck handleImpl(BacnetReadPropertyMultipleService service) throws Exception {
        List<String> command = service.toCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        log.debug("command: {}\nstdout:\n{}\nstderr:\n{}", command, stdout, stderr);
        if (StringUtils.hasText(stderr)) {
            throw new RuntimeException("ReadPropertyMultiple occurred error: " + stderr);
        }
        return jsonSerde.readValue(stdout, new TypeReference<ReadPropertyMultipleAck>() {
        }).orElse(null);
    }
}
