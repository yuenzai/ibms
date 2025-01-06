package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.*;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@ToString
public class DeviceDataAcquisitionView implements IDeviceDataAcquisition {
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private DeviceSchemas schemas;
    private Page<? extends Device> devices;

    protected DeviceDataAcquisitionView() {
    }

    public DeviceDataAcquisitionView(DeviceDataAcquisition dataAcquisition, Pageable pageable) {
        this.dataAcquisitionId = dataAcquisition.getDataAcquisitionId();
        this.schemas = dataAcquisition.getSchemas();
        List<? extends Device> devices = dataAcquisition.getDevices();
        this.devices = new PageImpl<>(devices, pageable, devices.size());
    }

    @Override
    public DeviceSchemas getSchemas() {
        return schemas;
    }

    @Override
    public Page<? extends Device> getDevices() {
        return devices;
    }
}
