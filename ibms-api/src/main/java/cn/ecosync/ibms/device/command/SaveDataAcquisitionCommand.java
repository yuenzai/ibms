package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.List;

@ToString
public class SaveDataAcquisitionCommand implements Command {
    @Valid
    @JsonUnwrapped
    protected DeviceDataAcquisitionId dataAcquisitionId;
    private Long scrapeInterval;

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }

    public Long getScrapeInterval() {
        return scrapeInterval;
    }

    @ToString(callSuper = true)
    public static class Bacnet extends SaveDataAcquisitionCommand {
        private final List<BacnetDataPoint> dataPoints;

        public Bacnet(DeviceDataAcquisitionId dataAcquisitionId, List<BacnetDataPoint> dataPoints) {
            Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
            Assert.notNull(dataPoints, "dataPoints must not be null");
            this.dataAcquisitionId = dataAcquisitionId;
            this.dataPoints = dataPoints;
        }

        public List<BacnetDataPoint> getDataPoints() {
            return dataPoints;
        }
    }
}
