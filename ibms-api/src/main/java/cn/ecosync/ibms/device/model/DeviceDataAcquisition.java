package cn.ecosync.ibms.device.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;
import org.springframework.util.Assert;

import static cn.ecosync.ibms.device.model.SynchronizationStateEnum.UNSYNCHRONIZED;

@ToString
public class DeviceDataAcquisition {
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private Integer scrapeInterval;
    private SynchronizationStateEnum synchronizationState;
    private DeviceDataPoints dataPoints;

    protected DeviceDataAcquisition() {
    }

    public DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId) {
        this(dataAcquisitionId, null);
    }

    public DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Integer scrapeInterval) {
        this(dataAcquisitionId, scrapeInterval, null, null);
    }

    public DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Integer scrapeInterval, DeviceDataPoints dataPoints, SynchronizationStateEnum synchronizationState) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        this.dataAcquisitionId = dataAcquisitionId;
        this.scrapeInterval = scrapeInterval != null ? scrapeInterval : 0;
        this.dataPoints = dataPoints != null ? dataPoints : DeviceDataPoints.Empty.INSTANCE;
        this.synchronizationState = synchronizationState != null ? synchronizationState : UNSYNCHRONIZED;
    }

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }

    public Integer getScrapeInterval() {
        return scrapeInterval;
    }

    public DeviceDataPoints getDataPoints() {
        return dataPoints;
    }

    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState;
    }

    @Deprecated
    public final DeviceDataAcquisition toReference() {
        return null;
    }

    public DeviceDataAcquisitionBuilder builder() {
        return new DeviceDataAcquisitionBuilder(this);
    }

    public static class DeviceDataAcquisitionBuilder {
        private final DeviceDataAcquisitionId dataAcquisitionId;
        private Integer scrapeInterval;
        private DeviceDataPoints dataPoints;
        private SynchronizationStateEnum synchronizationState;

        private DeviceDataAcquisitionBuilder(DeviceDataAcquisition dataAcquisition) {
            this(dataAcquisition.getDataAcquisitionId(), dataAcquisition.getScrapeInterval(), dataAcquisition.getSynchronizationState());
        }

        private DeviceDataAcquisitionBuilder(DeviceDataAcquisitionId dataAcquisitionId, Integer scrapeInterval, SynchronizationStateEnum synchronizationState) {
            this.dataAcquisitionId = dataAcquisitionId;
            this.scrapeInterval = scrapeInterval;
            this.synchronizationState = synchronizationState;
        }

        public DeviceDataAcquisitionBuilder with(Integer scrapeInterval) {
            if (scrapeInterval != null) {
                this.scrapeInterval = scrapeInterval;
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
            return new DeviceDataAcquisition(dataAcquisitionId, scrapeInterval, dataPoints, synchronizationState);
        }
    }
}
