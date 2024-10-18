package cn.ecosync.ibms.bacnet.service;

import cn.ecosync.ibms.bacnet.model.BacnetDeviceAddress;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetWhoIs {
    /**
     * bacnet-stack 默认的设备地址缓存文件名
     */
    public static final String ADDRESS_CACHE_FILENAME = "address_cache";

    public List<String> toCommand() {
        List<String> commands = new ArrayList<>();
        commands.add("whois");
        commands.add("|");
        commands.add("tee");
        commands.add(ADDRESS_CACHE_FILENAME);
        return commands;
    }

    private static final String HEX = "[0-9A-Fa-f]{2}";
    private static final String MAC = "(?:" + HEX + "[:-]" + ")" + "{0,5}" + HEX;
    private static final Pattern PATTERN = Pattern.compile("\\s+(\\d+)\\s+(" + MAC + ")\\s+(\\d+)\\s+(" + MAC + ")\\s+(\\d+)");

    public static List<BacnetDeviceAddress> parseDeviceAddresses(String whoIsOutPut) {
        String[] lines = whoIsOutPut.split("\n");
        return Arrays.stream(lines)
                .map(BacnetWhoIs::parseDeviceAddress)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static BacnetDeviceAddress parseDeviceAddress(String line) {
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
