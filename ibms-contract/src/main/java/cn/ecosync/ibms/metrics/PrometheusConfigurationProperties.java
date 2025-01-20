package cn.ecosync.ibms.metrics;

import cn.ecosync.iframework.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class PrometheusConfigurationProperties {
    public static final String relabel_configs =
            "    relabel_configs:\n" +
                    "      - source_labels: [ __address__ ]\n" +
                    "        target_label: __param_target\n" +
                    "      - source_labels: [ __param_target ]\n" +
                    "        target_label: instance\n" +
                    "      - target_label: __address__\n" +
                    "        replacement: localhost:9401\n";

    @Getter
    @ToString
    public static class ScrapeConfigs {
        private List<ScrapeConfig> scrapeConfigs;

        protected ScrapeConfigs() {
        }

        public ScrapeConfigs(ScrapeConfig... scrapeConfigs) {
            this.scrapeConfigs = Arrays.asList(scrapeConfigs);
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
        private Long scrapeInterval;
        private List<StaticConfig> staticConfigs;

        protected ScrapeConfig() {
        }

        public ScrapeConfig(String jobName, String metricsPath, Long scrapeInterval, StaticConfig... staticConfigs) {
            this.jobName = jobName;
            this.metricsPath = metricsPath;
            this.scrapeInterval = scrapeInterval;
            this.staticConfigs = Arrays.asList(staticConfigs);
        }
    }

    @Getter
    @ToString
    public static class StaticConfig {
        private List<String> targets;
        private Map<String, String> labels;

        protected StaticConfig() {
        }

        public StaticConfig(List<String> targets) {
            this(targets, Collections.emptyMap());
        }

        public StaticConfig(List<String> targets, Map<String, String> labels) {
            this.targets = targets;
            this.labels = labels;
        }

        public List<String> getTargets() {
            return CollectionUtils.nullSafeOf(targets);
        }

        public Map<String, String> getLabels() {
            return CollectionUtils.nullSafeOf(labels);
        }
    }

//    @Getter
//    @ToString
//    public static class RelabelConfig {
//        private List<String> sourceLabels;
//        private String targetLabel;
//        private String replacement;
//
//        protected RelabelConfig() {
//        }
//
//        public RelabelConfig(List<String> sourceLabels, String targetLabel, String replacement) {
//            this.sourceLabels = sourceLabels;
//            this.targetLabel = targetLabel;
//            this.replacement = replacement;
//        }
//    }
}
