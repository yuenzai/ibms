package cn.ecosync.ibms.bacnet.service;

import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.dto.ReadPropertyMultipleAck;
import cn.ecosync.ibms.bacnet.model.BacnetDevice;
import cn.ecosync.ibms.bacnet.model.BacnetSchemas;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.serde.TypeReference;
import cn.ecosync.iframework.util.CollectionUtils;
import cn.ecosync.iframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class BacnetService {
    private static final Logger log = LoggerFactory.getLogger(BacnetService.class);

    private final JsonSerde jsonSerde;

    public BacnetService(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    public ReadPropertyMultipleAck scrape(BacnetDevice device, BacnetSchemas bacnetSchemas) {
        Integer deviceInstance = device.getDeviceInstance();
        BacnetReadPropertyMultipleService service = BacnetReadPropertyMultipleService.newInstance(deviceInstance, bacnetSchemas);
        try {
            return scrape(service);
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    private ReadPropertyMultipleAck scrape(BacnetReadPropertyMultipleService service) throws Exception {
        List<String> command = service.toCommand();
        if (CollectionUtils.isEmpty(command)) return ReadPropertyMultipleAck.nullInstance(service.getDeviceInstance());

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        int exitValue = process.exitValue();
        if (exitValue != 0) log.error("{}", stderr);
        log.debug("command: {}, exitValue: {}\nstdout:\n{}\nstderr:\n{}", command, exitValue, stdout, stderr);
        if (StringUtils.hasText(stderr)) {
            throw new RuntimeException("ReadPropertyMultiple occurred error: " + stderr);
        }
        return jsonSerde.deserialize(stdout, new TypeReference<ReadPropertyMultipleAck>() {
        });
    }
}
