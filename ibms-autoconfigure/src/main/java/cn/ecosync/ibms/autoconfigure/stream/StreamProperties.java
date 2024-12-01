package cn.ecosync.ibms.autoconfigure.stream;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("stream")
public class StreamProperties {
    private String topicPrefix = "ibms";
}
