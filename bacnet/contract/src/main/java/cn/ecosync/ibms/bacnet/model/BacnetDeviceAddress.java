package cn.ecosync.ibms.bacnet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class BacnetDeviceAddress {
    private final Integer deviceInstance;
    private final String macAddress;
    private final Integer snet;
    private final String sadr;
    private final Integer apdu;
}
