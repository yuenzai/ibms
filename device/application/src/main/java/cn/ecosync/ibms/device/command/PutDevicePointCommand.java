package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.model.DevicePointProperties;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@ToString
public class PutDevicePointCommand implements Command {
    @NotBlank
    private String deviceCode;
    @Valid
    @NotEmpty
    private List<Point> devicePoints;

    public DeviceId toDeviceId() {
        return new DeviceId(this.deviceCode);
    }

    public List<Point> getDevicePoints() {
        return CollectionUtils.nullSafeOf(devicePoints);
    }

    @Getter
    @ToString
    public static class Point {
        @NotBlank
        private String pointCode;
        private String pointName;
        @Valid
        @NotNull
        private DevicePointProperties pointProperties;

        public DevicePointId toDevicePointId(String deviceCode) {
            return new DevicePointId(deviceCode, pointCode);
        }
    }
}
