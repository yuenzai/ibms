package cn.ecosync.ibms;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PropertyPlaceholderHelper;

public class Constants {
    public static final String AGGREGATE_TYPE_DEVICE = "device";
    public static final String AGGREGATE_TYPE_DEVICE_DAQ = "device-daq";
    public static final String AGGREGATE_TYPE_SCHEDULING = "scheduling";

    public static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER = new PropertyPlaceholderHelper("{", "}");
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
}
