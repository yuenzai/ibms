package cn.ecosync.ibms.gateway.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;
import org.springframework.util.Assert;

import static cn.ecosync.ibms.gateway.model.SynchronizationStateEnum.UNSYNCHRONIZED;

@ToString
public class DeviceDataAcquisition {
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private Integer scrapeInterval;
    private SynchronizationStateEnum synchronizationState;
    private DeviceInfos deviceInfos;
    private DeviceDataPoints dataPoints;

    protected DeviceDataAcquisition() {
    }

    public DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Integer scrapeInterval, DeviceInfos deviceInfos, DeviceDataPoints dataPoints, SynchronizationStateEnum synchronizationState) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        Assert.isTrue(scrapeInterval == null || (scrapeInterval >= 0 && scrapeInterval <= 120), "scrapeInterval must be between 0 and 120");
        this.dataAcquisitionId = dataAcquisitionId;
        this.scrapeInterval = scrapeInterval != null ? scrapeInterval : 0;
        this.deviceInfos = deviceInfos != null ? deviceInfos : DeviceInfos.EMPTY;
        this.dataPoints = dataPoints != null ? dataPoints : DeviceDataPoints.Empty.INSTANCE;
        this.synchronizationState = synchronizationState != null ? synchronizationState : UNSYNCHRONIZED;
    }

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }

    public Integer getScrapeInterval() {
        return scrapeInterval;
    }

    public DeviceInfos getDeviceInfos() {
        return deviceInfos;
    }

    public DeviceDataPoints getDataPoints() {
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
        private Integer scrapeInterval;
        private DeviceInfos deviceInfos;
        private DeviceDataPoints dataPoints;
        private SynchronizationStateEnum synchronizationState;

        private DeviceDataAcquisitionBuilder(DeviceDataAcquisition dataAcquisition) {
            this(dataAcquisition.getDataAcquisitionId(), dataAcquisition.getScrapeInterval(), dataAcquisition.getDeviceInfos(), dataAcquisition.getDataPoints(), dataAcquisition.getSynchronizationState());
        }

        private DeviceDataAcquisitionBuilder(DeviceDataAcquisitionId dataAcquisitionId, Integer scrapeInterval, DeviceInfos deviceInfos, DeviceDataPoints dataPoints, SynchronizationStateEnum synchronizationState) {
            this.dataAcquisitionId = dataAcquisitionId;
            this.scrapeInterval = scrapeInterval;
            this.deviceInfos = deviceInfos;
            this.dataPoints = dataPoints;
            this.synchronizationState = synchronizationState;
        }

        public DeviceDataAcquisitionBuilder with(Integer scrapeInterval) {
            if (scrapeInterval != null) {
                this.scrapeInterval = scrapeInterval;
            }
            return this;
        }

        public DeviceDataAcquisitionBuilder with(DeviceInfos deviceInfos) {
            if (deviceInfos != null) {
                this.deviceInfos = deviceInfos;
            }
            return this;
        }

        public DeviceDataAcquisitionBuilder with(DeviceDataPoints dataPoints) {
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
            return new DeviceDataAcquisition(dataAcquisitionId, scrapeInterval, deviceInfos, dataPoints, synchronizationState);
        }
    }
}
