package cn.ecosync.ibms.gateway.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;
import org.springframework.util.Assert;

import static cn.ecosync.ibms.gateway.model.SynchronizationStateEnum.UNSYNCHRONIZED;

@ToString
public class DeviceDataAcquisition {
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private DeviceDataAcquisitionType dataAcquisitionType;
    private Integer scrapeInterval;
    private Integer scrapeTimeout;
    private SynchronizationStateEnum synchronizationState;
    private LabelTable deviceInfos;
    private LabelTable dataPoints;

    protected DeviceDataAcquisition() {
    }

    public DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, DeviceDataAcquisitionType dataAcquisitionType, Integer scrapeInterval, Integer scrapeTimeout, LabelTable deviceInfos, LabelTable dataPoints, SynchronizationStateEnum synchronizationState) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        Assert.notNull(dataAcquisitionType, "dataAcquisitionType must not be null");
        Assert.isTrue(scrapeInterval == null || (scrapeInterval >= 0 && scrapeInterval <= 120), "scrapeInterval must be between 0 and 120");
        this.dataAcquisitionId = dataAcquisitionId;
        this.dataAcquisitionType = dataAcquisitionType;
        this.scrapeInterval = scrapeInterval != null ? scrapeInterval : 0;
        this.scrapeTimeout = scrapeTimeout != null ? scrapeTimeout : 0;
        this.deviceInfos = deviceInfos != null ? deviceInfos : LabelTable.EMPTY;
        this.dataPoints = dataPoints != null ? dataPoints : LabelTable.EMPTY;
        this.synchronizationState = synchronizationState != null ? synchronizationState : UNSYNCHRONIZED;
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

    public DeviceDataAcquisitionBuilder builder() {
        return new DeviceDataAcquisitionBuilder(this);
    }

    public static class DeviceDataAcquisitionBuilder {
        private final DeviceDataAcquisitionId dataAcquisitionId;
        private final DeviceDataAcquisitionType dataAcquisitionType;
        private Integer scrapeInterval;
        private Integer scrapeTimeout;
        private LabelTable deviceInfos;
        private LabelTable dataPoints;
        private SynchronizationStateEnum synchronizationState;

        private DeviceDataAcquisitionBuilder(DeviceDataAcquisition dataAcquisition) {
            this(dataAcquisition.getDataAcquisitionId(), dataAcquisition.getDataAcquisitionType(), dataAcquisition.getScrapeInterval(), dataAcquisition.getScrapeTimeout(), dataAcquisition.getDeviceInfos(), dataAcquisition.getDataPoints(), dataAcquisition.getSynchronizationState());
        }

        private DeviceDataAcquisitionBuilder(DeviceDataAcquisitionId dataAcquisitionId, DeviceDataAcquisitionType dataAcquisitionType, Integer scrapeInterval, Integer scrapeTimeout, LabelTable deviceInfos, LabelTable dataPoints, SynchronizationStateEnum synchronizationState) {
            this.dataAcquisitionId = dataAcquisitionId;
            this.dataAcquisitionType = dataAcquisitionType;
            this.scrapeInterval = scrapeInterval;
            this.scrapeTimeout = scrapeTimeout;
            this.deviceInfos = deviceInfos;
            this.dataPoints = dataPoints;
            this.synchronizationState = synchronizationState;
        }

        public DeviceDataAcquisitionBuilder withScrapeInterval(Integer scrapeInterval) {
            if (scrapeInterval != null) {
                this.scrapeInterval = scrapeInterval;
            }
            return this;
        }

        public DeviceDataAcquisitionBuilder withScrapeTimeout(Integer scrapeTimeout) {
            if (scrapeTimeout != null) {
                this.scrapeTimeout = scrapeTimeout;
            }
            return this;
        }

        public DeviceDataAcquisitionBuilder withDeviceInfos(LabelTable deviceInfos) {
            if (deviceInfos != null) {
                this.deviceInfos = deviceInfos;
            }
            return this;
        }

        public DeviceDataAcquisitionBuilder withDataPoints(LabelTable dataPoints) {
            if (dataPoints != null) {
                this.dataPoints = dataPoints;
            }
            return this;
        }

        public DeviceDataAcquisitionBuilder with(SynchronizationStateEnum synchronizationState) {
            if (synchronizationState != null) {
                this.synchronizationState = synchronizationState;
            }
            return this;
        }

        public DeviceDataAcquisition build() {
            return new DeviceDataAcquisition(dataAcquisitionId, dataAcquisitionType, scrapeInterval, scrapeTimeout, deviceInfos, dataPoints, synchronizationState);
        }
    }
}
