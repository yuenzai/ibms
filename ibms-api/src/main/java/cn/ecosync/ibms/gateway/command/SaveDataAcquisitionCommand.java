package cn.ecosync.ibms.gateway.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionType;
import cn.ecosync.ibms.gateway.model.LabelTable;
import cn.ecosync.ibms.gateway.model.SynchronizationStateEnum;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class SaveDataAcquisitionCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private DeviceDataAcquisitionType dataAcquisitionType;
    @Min(0)
    private Integer scrapeInterval;
    private Integer scrapeTimeout;
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

    public DeviceDataAcquisitionType getDataAcquisitionType() {
        return dataAcquisitionType;
    }

    public Integer getScrapeInterval() {
        return scrapeInterval;
    }

    public Integer getScrapeTimeout() {
        return scrapeTimeout;
    }

    public LabelTable getDeviceInfos() {
        return deviceInfos;
    }

    public LabelTable getDataPoints() {
        return dataPoints;
    }

    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState;
    }

    public SaveDataAcquisitionCommand withDeviceInfos(LabelTable deviceInfos) {
        this.deviceInfos = deviceInfos;
        return this;
    }

    public SaveDataAcquisitionCommand withDataPoints(DeviceDataAcquisitionType dataAcquisitionType, LabelTable dataPoints) {
        this.dataAcquisitionType = dataAcquisitionType;
        this.dataPoints = dataPoints;
        return this;
    }

    public SaveDataAcquisitionCommand withSynchronizationState(SynchronizationStateEnum synchronizationState) {
        this.synchronizationState = synchronizationState;
        return this;
    }

    @AssertTrue(message = "scrapeTimeout cannot be greater than the scrapeInterval")
    public boolean assertScrapeTimeout() {
        if (scrapeInterval != null && scrapeTimeout != null) {
            return scrapeTimeout <= scrapeInterval;
        }
        return true;
    }
}
