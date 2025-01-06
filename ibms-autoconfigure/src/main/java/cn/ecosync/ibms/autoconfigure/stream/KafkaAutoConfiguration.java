package cn.ecosync.ibms.autoconfigure.stream;

import cn.ecosync.ibms.Topics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Arrays;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnProperty(prefix = "spring.kafka", name = "bootstrap-servers")
@EnableConfigurationProperties(StreamProperties.class)
//@Import(KafkaStreamsConfiguration.class)
public class KafkaAutoConfiguration {
    private final StreamProperties streamProperties;

    public KafkaAutoConfiguration(StreamProperties streamProperties) {
        this.streamProperties = streamProperties;
    }

    @Bean
    public Topics topics() {
        return new Topics(streamProperties.getTopicPrefix());
    }

    @Bean
    public KafkaAdmin.NewTopics newTopics(Topics topics) {
        NewTopic[] newTopics = Arrays.stream(Topics.TopicEnum.values())
                .map(in -> newTopic(in, topics))
                .toArray(NewTopic[]::new);
        return new KafkaAdmin.NewTopics(newTopics);
    }

    private NewTopic newTopic(Topics.TopicEnum topicEnum, Topics topics) {
        return TopicBuilder.name(topics.getTopicName(topicEnum))
                .build();
    }
}
