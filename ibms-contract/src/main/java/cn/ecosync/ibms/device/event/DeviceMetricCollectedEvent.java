package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceMetric;
import cn.ecosync.iframework.event.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class DeviceMetricCollectedEvent extends AbstractEvent {
    @JsonUnwrapped
    private DeviceId deviceId;
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;
    private Long timestamp;
    private Map<String, DeviceMetric> metrics;

    protected DeviceMetricCollectedEvent() {
    }

    public DeviceMetricCollectedEvent(String eventDestination, DeviceId deviceId, DeviceDataAcquisitionId daqId, Long timestamp, Map<String, DeviceMetric> metrics) {
        super(eventDestination);
        this.deviceId = deviceId;
        this.daqId = daqId;
        this.timestamp = timestamp;
        this.metrics = metrics;
    }

    @Override
    public String eventKey() {
        return deviceId.toStringId();
    }
}
