package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionDto;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Optional;

@ToString
public class DeviceDataAcquisitionSavedEvent extends DeviceDataAcquisitionEvent {
    public static final String EVENT_TYPE = "device-daq-saved-event";

    @NotNull
    @JsonUnwrapped
    @JsonDeserialize(as = DeviceDataAcquisitionDto.class)
    private DeviceDataAcquisitionModel deviceDataAcquisitionModel;

    protected DeviceDataAcquisitionSavedEvent() {
    }

    public DeviceDataAcquisitionSavedEvent(DeviceDataAcquisitionModel deviceDataAcquisitionModel) {
        Assert.notNull(deviceDataAcquisitionModel, "deviceDataAcquisitionModel must not be null");
        this.deviceDataAcquisitionModel = deviceDataAcquisitionModel;
    }

    @Override
    public Optional<DeviceDataAcquisitionModel> daq() {
        return Optional.ofNullable(deviceDataAcquisitionModel);
    }

    @Override
    public String eventKey() {
        return deviceDataAcquisitionModel.getDaqId().toStringId();
    }
}
