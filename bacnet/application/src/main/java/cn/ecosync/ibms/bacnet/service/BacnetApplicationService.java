package cn.ecosync.ibms.bacnet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BacnetApplicationService {
    public static String readPropertyMultiple(String args) throws IOException, InterruptedException {
        String command = "readpropm " + args;
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();
        log.debug("Command will be execute: {}{}{}", args, System.lineSeparator(), stdout);
        if (exitCode != 0) {
            throw new RuntimeException("Bacnet: ReadPropertyMultiple error: " + stdout);
        }
        return stdout;
    }

    public static int readPropertyMultiple(String args, File dir) throws IOException, InterruptedException {
        if (!dir.exists()) {
            throw new IllegalArgumentException("dir does not exist: " + dir.getAbsolutePath());
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("dir is not a directory");
        }
        String command = "readpropm " + args;
        log.debug("Command will be execute: {}", command);
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
