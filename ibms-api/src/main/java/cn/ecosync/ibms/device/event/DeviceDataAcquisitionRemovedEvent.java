package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class DeviceDataAcquisitionRemovedEvent extends DeviceDataAcquisitionEvent {
    @JsonUnwrapped
    private DeviceDataAcquisition dataAcquisition;

    protected DeviceDataAcquisitionRemovedEvent() {
    }

    public DeviceDataAcquisitionRemovedEvent(DeviceDataAcquisition dataAcquisition) {
        Assert.notNull(dataAcquisition, "dataAcquisition must not be null");
        this.dataAcquisition = dataAcquisition;
    }

    @Override
    public String eventKey() {
        return dataAcquisition.getDataAcquisitionId().toString();
    }

    public DeviceDataAcquisition getDataAcquisition() {
        return dataAcquisition;
    }
}
