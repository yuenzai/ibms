package cn.ecosync.ibms.gateway.model;

import cn.ecosync.ibms.util.CollectionUtils;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
public class LabelValues {
    protected List<String> labelValues;

    protected LabelValues() {
    }

    public LabelValues(String... labelValues) {
        this(Arrays.asList(labelValues));
    }

    public LabelValues(List<String> labelValues) {
        this.labelValues = labelValues;
    }

    public List<String> getLabelValues() {
        return CollectionUtils.nullSafeOf(labelValues);
    }
}
