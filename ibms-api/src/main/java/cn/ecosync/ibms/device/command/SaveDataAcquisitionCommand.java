package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataPoints;
import cn.ecosync.ibms.device.model.SynchronizationStateEnum;
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
    private DeviceDataPoints dataPoints;
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

    public DeviceDataPoints getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(DeviceDataPoints dataPoints) {
        this.dataPoints = dataPoints;
    }

    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState;
    }

    public void setSynchronizationState(SynchronizationStateEnum synchronizationState) {
        this.synchronizationState = synchronizationState;
    }
}
