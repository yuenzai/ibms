package cn.ecosync.ibms.gateway.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.gateway.model.LabelTable;
import cn.ecosync.ibms.gateway.model.SynchronizationStateEnum;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class SaveDataAcquisitionCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    @Min(0)
    @Max(120)
    private Integer scrapeInterval;
    private LabelTable deviceInfos;
    private LabelTable dataPoints;
    private SynchronizationStateEnum synchronizationState;

    protected SaveDataAcquisitionCommand() {
    }

    public SaveDataAcquisitionCommand(DeviceDataAcquisitionId dataAcquisitionId) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        this.dataAcquisitionId = dataAcquisitionId;
    }

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }

    public Integer getScrapeInterval() {
        return scrapeInterval;
    }

    public void setScrapeInterval(Integer scrapeInterval) {
        this.scrapeInterval = scrapeInterval;
    }

    public LabelTable getDeviceInfos() {
        return deviceInfos;
    }

    public void setDeviceInfos(LabelTable deviceInfos) {
        this.deviceInfos = deviceInfos;
    }

    public LabelTable getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(LabelTable dataPoints) {
        this.dataPoints = dataPoints;
    }

    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState;
    }

    public void setSynchronizationState(SynchronizationStateEnum synchronizationState) {
        this.synchronizationState = synchronizationState;
    }
}
