package cn.ecosync.ibms.bacnet.service;

import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;
import cn.ecosync.ibms.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BacnetApplicationService {
    private final JsonSerde jsonSerde;

    public Optional<ReadPropertyMultipleAck> readPropertyMultiple(BacnetReadPropertyMultipleService service) throws Exception {
        List<String> command = service.toCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        log.debug("command:\n{}\nstdout:\n{}\nstderr:\n{}", command, stdout, stderr);
        if (StringUtils.hasText(stderr)) {
            throw new RuntimeException("BacnetReadPropertyMultipleService occurred error: " + stderr);
        }
        return jsonSerde.readValue(stdout, new TypeReference<ReadPropertyMultipleAck>() {
        });
    }

    public List<ReadPropertyMultipleAck> readPropertyMultiple(List<BacnetReadPropertyMultipleService> services) throws Exception {
        String command = services.stream()
                .map(BacnetReadPropertyMultipleService::toCommandString)
                .collect(Collectors.joining("; "));
        List<String> commands = Arrays.asList("/bin/bash", "-c", command);
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        log.debug("command:\n{}\nstdout:\n{}\nstderr:\n{}", command, stdout, stderr);
        return Arrays.stream(stdout.split("\n"))
                .map(in -> jsonSerde.readValue(in, new TypeReference<ReadPropertyMultipleAck>() {
                }).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public int readPropertyMultiple(BacnetReadPropertyMultipleService service, File dir) throws IOException, InterruptedException {
        if (!dir.exists()) {
            throw new IllegalArgumentException("dir does not exist: " + dir.getAbsolutePath());
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("dir is not a directory");
        }
        List<String> command = service.toCommand();
        log.debug("command will be execute: {}", command);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        File outFile = new File(dir, "out.log");
        File errorFile = new File(dir, "error.log");
        // 日志输出到文件
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(outFile));
        processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(errorFile));
        Process process = processBuilder.start();
        return process.waitFor();
    }
}
