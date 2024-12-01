package cn.ecosync.ibms;

import jakarta.validation.groups.Default;

public class Constants {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String AGGREGATE_TYPE_DEVICE = "device";
    public static final String AGGREGATE_TYPE_SCHEDULING = "scheduling";

    public static final String TOPIC_DEVICE = "device";
    public static final String TOPIC_DEVICE_SCHEMA = "device-schema";
    public static final String TOPIC_DEVICE_METRIC = "device-metric";

    public static final String TOPIC_READ_DEVICE_METRIC_COMMAND = "read-device-metric-command";
    public static final String TOPIC_READ_DEVICE_METRIC_WITH_SCHEMA_COMMAND = "read-device-metric-with-schema-command";

    public interface Create extends Default {
    }

    public interface Update extends Default {
    }
}
