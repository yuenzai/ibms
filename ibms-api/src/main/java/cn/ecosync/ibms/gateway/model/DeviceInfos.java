package cn.ecosync.ibms.gateway.model;

import io.prometheus.metrics.model.snapshots.Labels;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.Constants.LABEL_DEVICE_CODE;

public class DeviceInfos {
    private Map<String, Labels> deviceInfos;

    protected DeviceInfos() {
    }

    public DeviceInfos(LabelTable labelTable) {
        List<Labels> allLabels = labelTable.toLabels();
        this.deviceInfos = allLabels.stream()
                .collect(Collectors.toMap(in -> in.get(LABEL_DEVICE_CODE), Function.identity()));
    }

    public Labels get(String deviceCode) {
        return deviceInfos.get(deviceCode);
    }
}
