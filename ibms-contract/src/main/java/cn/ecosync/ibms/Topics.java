package cn.ecosync.ibms;

import cn.ecosync.iframework.util.StringUtils;

import static cn.ecosync.ibms.Constants.*;

public class Topics {
    private final String topicPrefix;

    public Topics(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getTopicName(TopicEnum topicEnum) {
        String topicName = topicEnum.toString();
        if (StringUtils.hasText(topicPrefix)) topicName = topicPrefix + "-" + topicName;
        return topicName;
    }

    public enum TopicEnum {
        TOPIC_DEVICE(AGGREGATE_TYPE_DEVICE),
        TOPIC_DEVICE_DAQ(AGGREGATE_TYPE_DEVICE_DAQ),
        TOPIC_DEVICE_GATEWAY(AGGREGATE_TYPE_DEVICE_GATEWAY),
        TOPIC_SCHEDULING(AGGREGATE_TYPE_DEVICE_GATEWAY),
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
            return name;
        }
    }
}
