package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.event.DeviceEventAggregator;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionDto;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CollectDeviceMetricCommand {
    private DeviceDataAcquisitionId daqId;
    @JsonDeserialize(as = DeviceDataAcquisitionDto.class)
    private DeviceDataAcquisitionModel daq;
    private DeviceEventAggregator aggregator;

    protected CollectDeviceMetricCommand() {
    }

    private CollectDeviceMetricCommand(CollectDeviceMetricCommand other) {
        this(other.daqId, other.daq, other.aggregator);
    }

    private CollectDeviceMetricCommand(DeviceDataAcquisitionId daqId, DeviceDataAcquisitionModel daq, DeviceEventAggregator aggregator) {
        this.daqId = daqId;
        this.daq = daq;
        this.aggregator = aggregator;
    }

    public CollectDeviceMetricCommand withDaqId(DeviceDataAcquisitionId daqId) {
        CollectDeviceMetricCommand command = new CollectDeviceMetricCommand(this);
        command.daqId = daqId;
        return command;
    }

    public CollectDeviceMetricCommand withDaq(DeviceDataAcquisitionModel daq) {
        CollectDeviceMetricCommand command = new CollectDeviceMetricCommand(this);
        command.daq = daq;
        return command;
    }

    public CollectDeviceMetricCommand withAggregator(DeviceEventAggregator aggregator) {
        CollectDeviceMetricCommand command = new CollectDeviceMetricCommand(this);
        command.aggregator = aggregator;
        return command;
    }
}
