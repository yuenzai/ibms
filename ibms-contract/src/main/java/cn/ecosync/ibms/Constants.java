package cn.ecosync.ibms;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PropertyPlaceholderHelper;

public class Constants {
    public static final String AGGREGATE_TYPE_DEVICE = "device";
    public static final String AGGREGATE_TYPE_DEVICE_DAQ = "device-daq";
    public static final String AGGREGATE_TYPE_SCHEDULING = "scheduling";

    public static final String TOPIC_DEVICE = AGGREGATE_TYPE_DEVICE;
    public static final String TOPIC_AGGREGATE_TYPE_DEVICE_DAQ = AGGREGATE_TYPE_DEVICE_DAQ;
    public static final String TOPIC_COLLECT_DEVICE_METRIC_COMMAND = "collect-device-metric-command";
    public static final String TOPIC_COLLECT_DEVICE_METRIC_COMMAND_JOINED = "collect-device-metric-command-joined";

    public static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER = new PropertyPlaceholderHelper("{", "}");
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
}
