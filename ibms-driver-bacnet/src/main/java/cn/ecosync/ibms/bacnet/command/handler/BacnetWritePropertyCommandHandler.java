package cn.ecosync.ibms.bacnet.command.handler;

import cn.ecosync.ibms.bacnet.command.BacnetWritePropertyCommand;
import cn.ecosync.ibms.bacnet.dto.BacnetWritePropertyService;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class BacnetWritePropertyCommandHandler implements CommandHandler<BacnetWritePropertyCommand> {
    @Override
    public void handle(BacnetWritePropertyCommand command) {
        try {
            BacnetWritePropertyService bacnetWriteProperty = command.getService();
            handleImpl(bacnetWriteProperty);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void handleImpl(BacnetWritePropertyService service) throws Exception {
        List<String> command = service.toCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
        log.debug("command: {}\nstdout:\n{}\nstderr:\n{}", command, stdout, stderr);
        if (StringUtils.hasText(stderr)) {
            throw new RuntimeException("WriteProperty occurred error: " + stderr);
        }
    }
}
