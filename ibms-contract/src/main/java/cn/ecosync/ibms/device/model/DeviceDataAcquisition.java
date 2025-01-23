package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import cn.ecosync.ibms.metrics.Instrument;
import cn.ecosync.iframework.serde.JsonSerde;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiConsumer;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetDataAcquisition.class, name = "BACNET"))
public class DeviceDataAcquisition implements IDeviceDataAcquisition {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    @NotNull
    private Long scrapeInterval;

    protected DeviceDataAcquisition() {
    }

    public DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        Assert.notNull(scrapeInterval, "scrapeInterval must not be null");
        this.dataAcquisitionId = dataAcquisitionId;
        this.scrapeInterval = scrapeInterval;
    }

    @Override
    public Long getScrapeInterval() {
        return scrapeInterval;
    }

    @Override
    public Collection<? extends DeviceDataPoint> getDataPoints() {
        return Collections.emptyList();
    }

    public DeviceDataAcquisition withScrapeInterval(Long scrapeInterval) {
        return new DeviceDataAcquisition(getDataAcquisitionId(), scrapeInterval);
    }

    public final DeviceDataAcquisition toReference() {
        return newReference(getDataAcquisitionId());
    }

    public static DeviceDataAcquisition newReference(DeviceDataAcquisitionId dataAcquisitionId) {
        DeviceDataAcquisition reference = new DeviceDataAcquisition();
        reference.dataAcquisitionId = dataAcquisitionId;
        return reference;
    }

    public void newInstruments(JsonSerde jsonSerde, BiConsumer<String, Instrument> consumer) {
    }
}
