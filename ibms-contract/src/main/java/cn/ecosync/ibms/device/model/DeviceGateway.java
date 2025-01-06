package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class DeviceGateway implements IDeviceGateway {
    @Valid
    @NotNull
    @JsonUnwrapped
    private DeviceGatewayId gatewayId;
    private List<DeviceDataAcquisition> dataAcquisitions;

    protected DeviceGateway() {
    }

    public DeviceGateway(DeviceGatewayId gatewayId, List<DeviceDataAcquisition> dataAcquisitions) {
        this.gatewayId = gatewayId;
        this.dataAcquisitions = dataAcquisitions;
    }

    @Override
    public List<DeviceDataAcquisition> getDataAcquisitions() {
        return CollectionUtils.nullSafeOf(dataAcquisitions);
    }

    public DeviceGateway withDataAcquisitions(List<DeviceDataAcquisition> dataAcquisitions) {
        return new DeviceGateway(getGatewayId(), dataAcquisitions);
    }
}
