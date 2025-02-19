package cn.ecosync.ibms;

import java.util.regex.Pattern;

public class Constants {
    public static final String REGEX_CODE = "[a-zA-Z_]\\w*";
    public static final Pattern PATTERN_CODE = Pattern.compile(REGEX_CODE);

    public static final String PATH_METRICS = "/metrics";
    public static final String PATH_METRICS_DEVICES = "/metrics/devices";

    public static final String LABEL_DEVICE_CODE = "device_code";
    public static final String LABEL_METRIC_NAME = "metric_name";
}
