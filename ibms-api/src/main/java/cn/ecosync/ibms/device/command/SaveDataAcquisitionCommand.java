package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.device.model.DeviceDataPoint;
import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.util.CollectionUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class SaveDataAcquisitionCommand implements Command {
    @NotBlank
    private String dataAcquisitionCode;
    private Long scrapeInterval;

    protected SaveDataAcquisitionCommand() {
    }

    public SaveDataAcquisitionCommand(String dataAcquisitionCode, Long scrapeInterval) {
        Assert.hasText(dataAcquisitionCode, "dataAcquisitionCode must not be null");
        this.dataAcquisitionCode = dataAcquisitionCode;
        this.scrapeInterval = scrapeInterval;
    }

    public List<? extends DeviceDataPoint> getDataPoints() {
        return Collections.emptyList();
    }

    @ToString(callSuper = true)
    public static class Bacnet extends SaveDataAcquisitionCommand {
        private final List<BacnetDataPoint> dataPoints;

        public Bacnet(String dataAcquisitionCode, Long scrapeInterval, List<BacnetDataPoint> dataPoints) {
            super(dataAcquisitionCode, scrapeInterval);
            this.dataPoints = dataPoints;
        }

        @Override
        public List<BacnetDataPoint> getDataPoints() {
            return CollectionUtils.nullSafeOf(dataPoints);
        }
    }
}
