package cn.ecosync.ibms.query.handler;

import cn.ecosync.ibms.bacnet.dto.BacnetDeviceAddress;
import cn.ecosync.ibms.bacnet.dto.BacnetWhoIsService;
import cn.ecosync.ibms.bacnet.query.BacnetWhoIsQuery;
import cn.ecosync.iframework.query.QueryHandler;
import cn.ecosync.iframework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BacnetWhoIsQueryHandler implements QueryHandler<BacnetWhoIsQuery, List<BacnetDeviceAddress>> {
    @Override
    public List<BacnetDeviceAddress> handle(BacnetWhoIsQuery query) {
        try {
            BacnetWhoIsService whoIs = query.getService();
            return handleImpl(whoIs);
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }

    private List<BacnetDeviceAddress> handleImpl(BacnetWhoIsService service) throws Exception {
        String command = service.toCommandString();
        List<String> commands = Arrays.asList("/bin/bash", "-c", command);
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        String workingDirectory = Optional.ofNullable(processBuilder.directory())
                .map(File::getAbsolutePath)
                .orElseGet(() -> System.getProperty("user.dir"));
        log.debug("command: {}, workingDirectory: {}\nstdout:\n{}\nstderr:\n{}", commands, workingDirectory, stdout, stderr);
        if (StringUtils.hasText(stderr)) {
            throw new RuntimeException("Who-Is occurred error: " + stderr);
        }
        return BacnetWhoIsService.parseDeviceAddresses(stdout);
    }
}
