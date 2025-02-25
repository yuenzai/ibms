package cn.ecosync.ibms.gateway.model;

import cn.ecosync.ibms.util.CollectionUtils;
import io.prometheus.metrics.model.snapshots.Labels;
import io.prometheus.metrics.model.snapshots.PrometheusNaming;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
public class LabelTable {
    public static final LabelTable EMPTY = new LabelTable();

    private Map<Integer, String> head;
    private List<Map<Integer, String>> body;

    protected LabelTable() {
    }

    public LabelTable(Map<Integer, String> head, List<Map<Integer, String>> body) {
        this.head = head;
        this.body = body;
    }

    public List<String> toLabelNames() {
        if (CollectionUtils.isEmpty(getHead())) {
            return Collections.emptyList();
        }
        List<String> labelNames = new ArrayList<>(getHead().size());
        for (int i = 0; i < getHead().size(); i++) {
            String labelName = getHead().get(i);
            labelNames.add(labelName);
        }
        return labelNames;
    }

    public List<LabelValues> toLabelValues() {
        if (CollectionUtils.isEmpty(getHead())) {
            return Collections.emptyList();
        }
        List<LabelValues> body = new ArrayList<>(getBody().size());
        for (Map<Integer, String> row : getBody()) {
            List<String> labelValues = new ArrayList<>(getHead().size());
            for (int i = 0; i < getHead().size(); i++) {
                String labelValue = row.get(i);
                labelValues.add(labelValue);
            }
            body.add(new LabelValues(labelValues));
        }
        return body;
    }

    public List<Labels> toLabels() {
        List<Labels> labelsList = new ArrayList<>(getBody().size());
        List<String> labelNames = toLabelNames().stream()
                .map(PrometheusNaming::sanitizeMetricName)
                .collect(Collectors.toList());
        for (LabelValues deviceInfo : toLabelValues()) {
            List<String> labelValues = deviceInfo.getLabelValues();
            Labels labels = Labels.of(labelNames, labelValues);
            labelsList.add(labels);
        }
        return labelsList;
    }

//    public <T extends LabelValues> List<T> toLabels(BiFunction<Map<String, Integer>, List<String>, T> labelsFunction) {
//        if (CollectionUtils.isEmpty(getHead())) {
//            return Collections.emptyList();
//        }
//        Map<String, Integer> reversedHead = getHead().entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
//        List<T> body = new ArrayList<>(getBody().size());
//        for (Map<Integer, String> row : getBody()) {
//            List<String> labelValues = new ArrayList<>(getHead().size());
//            for (int i = 0; i < getHead().size(); i++) {
//                String labelValue = row.get(i);
//                labelValues.add(labelValue);
//            }
//            body.add(labelsFunction.apply(reversedHead, labelValues));
//        }
//        return body;
//    }

    public Stream<String> get(String name) {
        Integer index = null;
        for (Map.Entry<Integer, String> entry : getHead().entrySet()) {
            if (Objects.equals(entry.getValue(), name)) {
                index = entry.getKey();
                break;
            }
        }
        Assert.notNull(index, "label not found: " + name);
        return get(index);
    }

    public Stream<String> get(Integer index) {
        return getBody().stream()
                .map(in -> in.get(index))
                .filter(Objects::nonNull);
    }

    public Integer size() {
        return getBody().size();
    }

    public Map<Integer, String> getHead() {
        return CollectionUtils.nullSafeOf(head);
    }

    public List<Map<Integer, String>> getBody() {
        return CollectionUtils.nullSafeOf(body);
    }
}
