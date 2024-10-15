package cn.ecosync.ibms.bacnet.service;

import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class BacnetApplicationService {
    public static String readPropertyMultiple(BacnetReadPropertyMultipleService service) throws IOException, InterruptedException {
        List<String> command = service.toCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();
        log.debug("command will be execute: {}{}{}", command, System.lineSeparator(), stdout);
        if (exitCode != 0) {
            throw new RuntimeException("bacnet: ReadPropertyMultiple error: " + stdout);
        }
        return stdout;
    }

    public static int readPropertyMultiple(BacnetReadPropertyMultipleService service, File dir) throws IOException, InterruptedException {
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
