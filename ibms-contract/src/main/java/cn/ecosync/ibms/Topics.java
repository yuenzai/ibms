package cn.ecosync.ibms;

import cn.ecosync.iframework.util.StringUtils;
import cn.ecosync.iframework.util.ToStringId;

public class Topics {
    private final String topicPrefix;

    public Topics(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getTopicName(TopicEnum topicEnum) {
        String topicName = topicEnum.toStringId();
        if (StringUtils.hasText(topicPrefix)) topicName = topicPrefix + "-" + topicName;
        return topicName;
    }

    public enum TopicEnum implements ToStringId {
        TOPIC_DEVICE("device"),
        TOPIC_AGGREGATE_TYPE_DEVICE_DAQ("device-daq"),
        TOPIC_COLLECT_DEVICE_METRIC_COMMAND("collect-device-metric-command"),
        TOPIC_COLLECT_DEVICE_METRIC_ENHANCED_COMMAND("collect-device-metric-enhanced-command"),
        TOPIC_DEVICE_METRIC_COLLECTED_EVENT("device-metric-collected-event"),
        ;

        private final String name;

        TopicEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return toStringId();
        }

        @Override
        public String toStringId() {
            return name;
        }
    }
}
