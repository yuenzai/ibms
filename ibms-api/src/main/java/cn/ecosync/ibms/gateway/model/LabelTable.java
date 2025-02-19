package cn.ecosync.ibms.gateway.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataPoints;
import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.prometheus.metrics.model.registry.MultiCollector;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetDataPoints.class, name = "BACNET"),
})
public class LabelTable {
    public static final LabelTable EMPTY = new LabelTable();

    protected Map<Integer, String> head;
    protected List<Map<Integer, String>> body;

    protected LabelTable() {
    }

    public LabelTable(Map<Integer, String> head, List<Map<Integer, String>> body) {
        Assert.notNull(head, "head must not be null");
        Assert.notNull(body, "body must not be null");
        this.head = head;
        this.body = body;
    }

    public Map<Integer, String> getHead() {
        return head;
    }

    public List<Map<Integer, String>> getBody() {
        return body;
    }

    public List<String> toLabelNames() {
        if (CollectionUtils.isEmpty(head)) {
            return Collections.emptyList();
        }
        List<String> labelNames = new ArrayList<>(head.size());
        for (int i = 0; i < head.size(); i++) {
            String labelName = head.get(i);
            labelNames.add(labelName);
        }
        return labelNames;
    }

    public List<? extends LabelValues> toLabelValues() {
        if (CollectionUtils.isEmpty(head)) {
            return Collections.emptyList();
        }
        List<LabelValues> body = new ArrayList<>(this.body.size());
        for (Map<Integer, String> row : this.body) {
            List<String> labelValues = new ArrayList<>(head.size());
            for (int i = 0; i < head.size(); i++) {
                String labelValue = row.get(i);
                labelValues.add(labelValue);
            }
            body.add(new LabelValues(labelValues));
        }
        return body;
    }

    public Stream<String> get(String name) {
        Integer index = null;
        for (Map.Entry<Integer, String> entry : head.entrySet()) {
            if (Objects.equals(entry.getValue(), name)) {
                index = entry.getKey();
                break;
            }
        }
        Assert.notNull(index, "label not found: " + name);
        return get(index);
    }

    public Stream<String> get(Integer index) {
        return body.stream()
                .map(in -> in.get(index))
                .filter(Objects::nonNull);
    }

    public Integer size() {
        return body.size();
    }

    public void newInstruments(BiConsumer<String, MultiCollector> consumer) {
    }
}
