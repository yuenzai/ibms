package cn.ecosync.ibms.gateway.model;

import cn.ecosync.ibms.util.CollectionUtils;
import io.prometheus.metrics.core.metrics.Info;
import io.prometheus.metrics.model.snapshots.PrometheusNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ToString
public class DeviceInfos {
    public static final DeviceInfos EMPTY = new DeviceInfos();

    private List<@Valid DeviceInfo> deviceInfos;

    protected DeviceInfos() {
    }

    public DeviceInfos(DeviceInfo... deviceInfos) {
        this(Arrays.asList(deviceInfos));
    }

    public DeviceInfos(List<DeviceInfo> deviceInfos) {
        this.deviceInfos = deviceInfos;
    }

    public List<DeviceInfo> getDeviceInfos() {
        return CollectionUtils.nullSafeOf(deviceInfos);
    }

    @ToString
    public static class DeviceInfo {
        @NotBlank
        private String deviceCode;
        private List<@Valid DeviceDataPointLabel> labels;

        protected DeviceInfo() {
        }

        public DeviceInfo(String deviceCode, DeviceDataPointLabel... labels) {
            this(deviceCode, Arrays.asList(labels));
        }

        public DeviceInfo(String deviceCode, List<DeviceDataPointLabel> labels) {
            this.deviceCode = deviceCode;
            this.labels = labels;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public List<DeviceDataPointLabel> getLabels() {
            return Collections.unmodifiableList(CollectionUtils.nullSafeOf(labels));
        }

        public Info toDeviceInfo() {
            Info.Builder deviceInfoBuilder = Info.builder()
                    .name("device")
                    .help("Device Info");
            deviceInfoBuilder.labelNames(toLabelNames());
            Info deviceInfo = deviceInfoBuilder.build();
            deviceInfo.setLabelValues(toLabelValues());
            return deviceInfo;
        }

        private String[] toLabelNames() {
            List<DeviceDataPointLabel> labels = getLabels();
            String[] names = new String[labels.size() + 1];
            names[0] = "device_code";
            for (int i = 1; i <= labels.size(); i++) {
                names[i] = PrometheusNaming.sanitizeMetricName(labels.get(i - 1).getName());
            }
            return names;
        }

        public String[] toLabelValues() {
            List<DeviceDataPointLabel> labels = getLabels();
            String[] values = new String[labels.size() + 1];
            values[0] = deviceCode;
            for (int i = 1; i <= labels.size(); i++) {
                values[i] = labels.get(i - 1).getValue();
            }
            return values;
        }
    }
}
