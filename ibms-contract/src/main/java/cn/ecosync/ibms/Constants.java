package cn.ecosync.ibms;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PropertyPlaceholderHelper;

public class Constants {
    public static final String AGGREGATE_TYPE_DEVICE = "outbox-device";
    public static final String AGGREGATE_TYPE_DEVICE_DAQ = "outbox-device-daq";
    public static final String AGGREGATE_TYPE_DEVICE_GATEWAY = "outbox-device-gateway";
    public static final String AGGREGATE_TYPE_SCHEDULING = "outbox-scheduling";
    public static final String WEBSOCKET_TOPIC_GATEWAY_UPDATES = "/queue/gateway-updates";// 客户端需要订阅/user/queue/gateway-updates

    public static final PropertyPlaceholderHelper PROPERTY_PLACEHOLDER = new PropertyPlaceholderHelper("{", "}");
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
}
