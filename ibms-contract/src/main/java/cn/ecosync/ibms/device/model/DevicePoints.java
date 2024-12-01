package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.util.CollectionUtils;
import cn.ecosync.iframework.util.StringUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
public class DevicePoints {
    @Valid
    private List<DevicePoint> devicePoints;

    protected DevicePoints() {
    }

    public DevicePoints(List<DevicePoint> devicePoints) {
        List<DevicePoint> nullSafeVar = CollectionUtils.nullSafeOf(devicePoints);
        // check
        Set<String> ignored = nullSafeVar.stream()
                .map(DevicePoint::getPointCode)
                .collect(Collectors.toSet());
        this.devicePoints = nullSafeVar;
    }

    @Getter
    @ToString
    public static class DevicePoint {
        @NotBlank
        private String pointCode;
        private String pointName;
        @Valid
        @NotNull
        private DevicePointExtra pointExtra;

        protected DevicePoint() {
        }

        public DevicePoint(String pointCode, String pointName, DevicePointExtra pointExtra) {
            Assert.hasText(pointCode, "pointCode can not be empty");
            Assert.notNull(pointExtra, "pointExtra can not be null");
            this.pointCode = pointCode;
            this.pointName = StringUtils.nullSafeOf(pointName);
            this.pointExtra = pointExtra;
        }
    }
}
