package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.ToString;

import java.util.List;

@ToString
public class SaveGatewayCommand implements GatewayCommand {
    private DeviceGatewayId gatewayId;
    private List<String> dataAcquisitionCodes;

    protected SaveGatewayCommand() {
    }

    public SaveGatewayCommand(List<String> dataAcquisitionCodes) {
        this(null, dataAcquisitionCodes);
    }

    private SaveGatewayCommand(DeviceGatewayId gatewayId, List<String> dataAcquisitionCodes) {
        this.gatewayId = gatewayId;
        this.dataAcquisitionCodes = dataAcquisitionCodes;
    }

    @Override
    public DeviceGatewayId gatewayId() {
        return gatewayId;
    }

    public List<String> getDataAcquisitionCodes() {
        return CollectionUtils.nullSafeOf(dataAcquisitionCodes);
    }

    @Override
    public SaveGatewayCommand withGatewayId(DeviceGatewayId gatewayId) {
        return new SaveGatewayCommand(gatewayId, dataAcquisitionCodes);
    }
}
