package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.iframework.util.ToStringId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class DeviceMetricCollectedEvent implements ToStringId {
    @JsonUnwrapped
    private DeviceId deviceId;
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;
    private Long timestamp;
    private Map<String, Object> values;

    protected DeviceMetricCollectedEvent() {
    }

    public DeviceMetricCollectedEvent(DeviceId deviceId, DeviceDataAcquisitionId daqId, Long timestamp, Map<String, Object> values) {
        this.deviceId = deviceId;
        this.daqId = daqId;
        this.timestamp = timestamp;
        this.values = values;
    }

    @Override
    public String toStringId() {
        return deviceId.toStringId();
    }
}
