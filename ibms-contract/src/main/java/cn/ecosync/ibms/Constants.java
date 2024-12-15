package cn.ecosync.ibms;

import jakarta.validation.groups.Default;

public class Constants {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    // 匹配字符 ['A-Z', 'a-z', '0-9', '-'] 1~16次
    public static final String REGEX_CODE = "[A-Za-z0-9-]{1,16}";

    public static final String AGGREGATE_TYPE_DEVICE = "device";
    public static final String AGGREGATE_TYPE_DEVICE_SCHEMA = "device-schema";
    public static final String AGGREGATE_TYPE_DEVICE_DAQ = "device-daq";
    public static final String AGGREGATE_TYPE_SCHEDULING = "scheduling";

    public static final String TOPIC_DEVICE = AGGREGATE_TYPE_DEVICE;
    public static final String TOPIC_DEVICE_SCHEMA = AGGREGATE_TYPE_DEVICE_SCHEMA;
    public static final String TOPIC_AGGREGATE_TYPE_DEVICE_DAQ = AGGREGATE_TYPE_DEVICE_DAQ;
    public static final String TOPIC_COLLECT_DEVICE_METRIC_COMMAND = "collect-device-metric-command";
    public static final String TOPIC_COLLECT_DEVICE_METRIC_COMMAND_JOINED = "collect-device-metric-command-joined";

    public interface Create extends Default {
    }

    public interface Update extends Default {
    }
}
