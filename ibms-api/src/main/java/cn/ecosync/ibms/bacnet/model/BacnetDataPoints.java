package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.gateway.model.LabelTable;
import cn.ecosync.ibms.util.CollectionUtils;
import io.prometheus.metrics.model.registry.MultiCollector;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@ToString
public class BacnetDataPoints extends LabelTable {
    protected BacnetDataPoints() {
    }

    public BacnetDataPoints(Map<Integer, String> head, List<Map<Integer, String>> body) {
        super(head, body);
    }

    @Override
    public List<BacnetDataPoint> toLabelValues() {
        if (CollectionUtils.isEmpty(head)) {
            return Collections.emptyList();
        }
        List<BacnetDataPoint> body = new ArrayList<>(this.body.size());
        for (Map<Integer, String> row : this.body) {
            List<String> labelValues = new ArrayList<>(head.size());
            for (int i = 0; i < head.size(); i++) {
                String labelValue = row.get(i);
                labelValues.add(labelValue);
            }
            body.add(new BacnetDataPoint(head, labelValues));
        }
        return body;
    }

    @Override
    public void newInstruments(BiConsumer<String, MultiCollector> consumer) {
        toLabelValues().stream()
                .collect(Collectors.groupingBy(in -> in.getDataPointId().getDeviceCode()))
                .forEach((deviceCode, dataPoints) -> consumer.accept(deviceCode, new BacnetInstrumentation(dataPoints)));
    }
}
