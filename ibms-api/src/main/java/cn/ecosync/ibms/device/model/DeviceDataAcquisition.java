package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition.BacnetDataAcquisitionBuilder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.prometheus.metrics.model.registry.MultiCollector;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiConsumer;

import static cn.ecosync.ibms.device.model.IDeviceDataAcquisition.SynchronizationStateEnum.UNSYNCHRONIZED;

@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = BacnetDataAcquisition.class, name = "BACNET")})
public class DeviceDataAcquisition implements IDeviceDataAcquisition {
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private Long scrapeInterval;
    private SynchronizationStateEnum synchronizationState;

    protected DeviceDataAcquisition() {
    }

    public DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval, SynchronizationStateEnum synchronizationState) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        Assert.notNull(scrapeInterval, "scrapeInterval must not be null");
        this.dataAcquisitionId = dataAcquisitionId;
        this.scrapeInterval = scrapeInterval;
        this.synchronizationState = synchronizationState != null ? synchronizationState : UNSYNCHRONIZED;
    }

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }

    @Override
    public Long getScrapeInterval() {
        return scrapeInterval;
    }

    @Override
    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState;
    }

    @Override
    public Collection<? extends DeviceDataPoint> getDataPoints() {
        return Collections.emptyList();
    }

    @Deprecated
    public final DeviceDataAcquisition toReference() {
        return null;
    }

    public void newInstruments(BiConsumer<String, MultiCollector> consumer) {
    }

    public DeviceDataAcquisitionBuilder builder() {
        return new DeviceDataAcquisitionBuilder(this);
    }

    public static class DeviceDataAcquisitionBuilder {
        protected DeviceDataAcquisitionId dataAcquisitionId;
        protected Long scrapeInterval;
        protected SynchronizationStateEnum synchronizationState;

        public DeviceDataAcquisitionBuilder(DeviceDataAcquisition dataAcquisition) {
            this(dataAcquisition.getDataAcquisitionId(), dataAcquisition.getScrapeInterval(), dataAcquisition.getSynchronizationState());
        }

        public DeviceDataAcquisitionBuilder(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval, SynchronizationStateEnum synchronizationState) {
            this.dataAcquisitionId = dataAcquisitionId;
            this.scrapeInterval = scrapeInterval;
            this.synchronizationState = synchronizationState;
        }

        public DeviceDataAcquisitionBuilder with(Long scrapeInterval) {
            if (scrapeInterval != null) {
                this.scrapeInterval = scrapeInterval;
            }
            return this;
        }

        public DeviceDataAcquisitionBuilder with(SynchronizationStateEnum synchronizationState) {
            if (synchronizationState != null) {
                this.synchronizationState = synchronizationState;
            }
            return this;
        }

        public BacnetDataAcquisitionBuilder asBacnetBuilder() {
            return new BacnetDataAcquisitionBuilder(dataAcquisitionId, scrapeInterval, synchronizationState, Collections.emptyList());
        }

        public DeviceDataAcquisition build() {
            return new DeviceDataAcquisition(dataAcquisitionId, scrapeInterval, synchronizationState);
        }
    }
}
