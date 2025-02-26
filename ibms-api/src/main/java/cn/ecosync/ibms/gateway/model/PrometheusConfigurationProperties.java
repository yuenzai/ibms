package cn.ecosync.ibms.gateway.model;

import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
public class PrometheusConfigurationProperties {
    @Getter
    @ToString
    public static class ScrapeConfigs {
        private List<ScrapeConfig> scrapeConfigs;

        protected ScrapeConfigs() {
        }

        public ScrapeConfigs(List<ScrapeConfig> scrapeConfigs) {
            this.scrapeConfigs = scrapeConfigs;
        }

        public List<ScrapeConfig> getScrapeConfigs() {
            return CollectionUtils.nullSafeOf(scrapeConfigs);
        }
    }

    @Getter
    @ToString
    public static class ScrapeConfig {
        private String jobName;
        private String metricsPath;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean honorLabels;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String scrapeInterval;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String scrapeTimeout;
        private List<RelabelConfig> relabelConfigs;
        private List<StaticConfig> staticConfigs;

        protected ScrapeConfig() {
        }

        public ScrapeConfig(String jobName, String metricsPath, Integer scrapeInterval, Integer scrapeTimeout, List<RelabelConfig> relabelConfigs, StaticConfig... staticConfigs) {
            this(jobName, metricsPath, null, scrapeInterval, scrapeTimeout, relabelConfigs, staticConfigs);
        }

        public ScrapeConfig(String jobName, String metricsPath, Boolean honorLabels, Integer scrapeInterval, Integer scrapeTimeout, List<RelabelConfig> relabelConfigs, StaticConfig... staticConfigs) {
            this.jobName = jobName;
            this.metricsPath = metricsPath;
            this.honorLabels = honorLabels;
            this.scrapeInterval = Optional.ofNullable(scrapeInterval)
                    .map(in -> in + "s")
                    .orElse(null);
            this.scrapeTimeout = Optional.ofNullable(scrapeTimeout)
                    .map(in -> in + "s")
                    .orElse(null);
            this.relabelConfigs = relabelConfigs;
            this.staticConfigs = Arrays.asList(staticConfigs);
        }

        public List<RelabelConfig> getRelabelConfigs() {
            return CollectionUtils.nullSafeOf(relabelConfigs);
        }

        public List<StaticConfig> getStaticConfigs() {
            return CollectionUtils.nullSafeOf(staticConfigs);
        }
    }

    @Getter
    @ToString
    public static class StaticConfig {
        private Collection<String> targets;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private Map<String, String> labels;

        protected StaticConfig() {
        }

        public StaticConfig(String... targets) {
            this(Collections.emptyMap(), targets);
        }

        public StaticConfig(Map<String, String> labels, String... targets) {
            this.targets = Arrays.asList(targets);
            this.labels = labels;
        }

        public Collection<String> getTargets() {
            return CollectionUtils.nullSafeOf(targets);
        }

        public Map<String, String> getLabels() {
            return CollectionUtils.nullSafeOf(labels);
        }
    }

    @Getter
    @ToString
    public static class RelabelConfig {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<String> sourceLabels;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String targetLabel;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String replacement;

        protected RelabelConfig() {
        }

        private RelabelConfig(List<String> sourceLabels, String targetLabel, String replacement) {
            this.sourceLabels = sourceLabels;
            this.targetLabel = targetLabel;
            this.replacement = replacement;
        }

        public static List<RelabelConfig> toRelabelConfigs(String targetLabel, String replacement) {
            targetLabel = Optional.ofNullable(targetLabel).filter(StringUtils::hasText).orElse("instance");
            RelabelConfig RELABEL1 = new RelabelConfig(Collections.singletonList("__address__"), "__param_target", null);
            RelabelConfig RELABEL2 = new RelabelConfig(Collections.singletonList("__param_target"), targetLabel, null);
            RelabelConfig RELABEL3 = new RelabelConfig(null, "__address__", replacement);
            return Arrays.asList(RELABEL1, RELABEL2, RELABEL3);
        }
    }
}
