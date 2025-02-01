package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.prometheus.metrics.core.metrics.Info;
import io.prometheus.metrics.model.snapshots.PrometheusNaming;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetDataPoint.class, name = "BACNET"))
public abstract class DeviceDataPoint {
    @Valid
    @JsonUnwrapped
    private DeviceDataPointId dataPointId;
    private List<@Valid DeviceDataPointLabel> labels;

    protected DeviceDataPoint() {
    }

    public DeviceDataPoint(DeviceDataPointId dataPointId, List<DeviceDataPointLabel> labels) {
        this.dataPointId = dataPointId;
        this.labels = labels;
    }

    public List<DeviceDataPointLabel> getLabels() {
        return Collections.unmodifiableList(CollectionUtils.nullSafeOf(labels));
    }

    public Info toPointInfo() {
        Info.Builder pointInfoBuilder = Info.builder()
                .name("point")
                .help("Point Info");
        pointInfoBuilder.labelNames(toLabelNames());
        return pointInfoBuilder.build();
    }

    private String[] toLabelNames() {
        List<DeviceDataPointLabel> labels = getLabels();
        String[] names = new String[labels.size() + 1];
        names[0] = "point_name";
        for (int i = 1; i <= labels.size(); i++) {
            names[i] = PrometheusNaming.sanitizeMetricName(labels.get(i - 1).getName());
        }
        return names;
    }

    public String[] toLabelValues() {
        List<DeviceDataPointLabel> labels = getLabels();
        String[] values = new String[labels.size() + 1];
        values[0] = getDataPointId().getPointName();
        for (int i = 1; i <= labels.size(); i++) {
            values[i] = labels.get(i - 1).getValue();
        }
        return values;
    }
}
