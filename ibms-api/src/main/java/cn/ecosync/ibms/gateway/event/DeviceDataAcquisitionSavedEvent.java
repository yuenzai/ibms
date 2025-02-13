package cn.ecosync.ibms.gateway.event;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class DeviceDataAcquisitionSavedEvent extends DeviceDataAcquisitionEvent {
    @JsonUnwrapped
    private DeviceDataAcquisition dataAcquisition;

    protected DeviceDataAcquisitionSavedEvent() {
    }

    public DeviceDataAcquisitionSavedEvent(DeviceDataAcquisition dataAcquisition) {
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
