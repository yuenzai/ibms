package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.ibms.util.StringUtils;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@ToString
public class BacnetWhoIsService {
    /**
     * bacnet-stack 默认的设备地址缓存文件名
     */
    public static final String ADDRESS_CACHE_FILENAME = "address_cache";
    private static final String HEX = "[0-9A-Fa-f]{2}";
    private static final String MAC = "(?:" + HEX + "[:-]" + ")" + "{0,5}" + HEX;
    private static final Pattern PATTERN = Pattern.compile("\\s+(\\d+)\\s+(" + MAC + ")\\s+(\\d+)\\s+(" + MAC + ")\\s+(\\d+)");

    private List<String> toCommand() {
        List<String> commands = new ArrayList<>();
        commands.add("whois");
        commands.add("|");
        commands.add("tee");
        commands.add(ADDRESS_CACHE_FILENAME);
        return commands;
    }

    public static List<BacnetDeviceAddress> execute(BacnetWhoIsService service) throws Exception {
        List<String> command = service.toCommand();
        String commandString = String.join(" ", command);
        log.info("Execute command[{}]", commandString);
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", commandString);
        Process process = processBuilder.start();
        String stdout = StreamUtils.copyToString(process.getInputStream(), StandardCharsets.UTF_8);
        String stderr = StreamUtils.copyToString(process.getErrorStream(), StandardCharsets.UTF_8);
        process.waitFor();
//        String workingDirectory = Optional.ofNullable(processBuilder.directory())
//                .map(File::getAbsolutePath)
//                .orElseGet(() -> System.getProperty("user.dir"));
        if (StringUtils.hasText(stdout)) log.info("\n{}", stdout);
        if (StringUtils.hasText(stderr)) throw new RuntimeException(stderr);
        return BacnetWhoIsService.parseDeviceAddresses(stdout);
    }

    private static List<BacnetDeviceAddress> parseDeviceAddresses(String whoIsOutPut) {
        String[] lines = whoIsOutPut.split("\n");
        return Arrays.stream(lines)
                .map(BacnetWhoIsService::parseDeviceAddress)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static BacnetDeviceAddress parseDeviceAddress(String line) {
        if (line.startsWith(";")) {
            return null;
        }
        Matcher matcher = PATTERN.matcher(line);
        boolean found = matcher.find();
        if (!found) {
            return null;
        }
        Integer deviceInstance = Integer.parseInt(matcher.group(1));
        String macAddress = matcher.group(2);
        Integer snet = Integer.parseInt(matcher.group(3));
        String sadr = matcher.group(4);
        Integer apdu = Integer.parseInt(matcher.group(5));
        return new BacnetDeviceAddress(deviceInstance, macAddress, snet, sadr, apdu);
    }
}
