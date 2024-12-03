package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.util.CollectionUtils;
import cn.ecosync.iframework.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.apache.avro.Schema;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
public class DevicePoints implements Iterable<DevicePoints.DevicePoint> {
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

    @Override
    public Iterator<DevicePoint> iterator() {
        return devicePoints.iterator();
    }

    @Getter
    @ToString
    public static class DevicePoint {
        @NotBlank
        private String pointCode;
        private String pointName;
        @NotNull
        private DevicePointType pointType;
        private String defaultValue;
        @Valid
        @NotNull
        private DevicePointExtra pointExtra;

        protected DevicePoint() {
        }

        public DevicePoint(String pointCode, String pointName, DevicePointType pointType, String defaultValue, DevicePointExtra pointExtra) {
            Assert.hasText(pointCode, "pointCode can not be empty");
            Assert.notNull(pointType, "pointType can not be null");
            Assert.notNull(pointExtra, "pointExtra can not be null");
            this.pointCode = pointCode;
            this.pointName = StringUtils.nullSafeOf(pointName);
            this.pointType = pointType;
            this.defaultValue = defaultValue;
            this.pointExtra = pointExtra;
        }

        public Optional<String> getDefaultValue() {
            return Optional.ofNullable(defaultValue);
        }
    }

    @ToString
    public enum DevicePointType {
        INT("int", Schema.Type.INT),
        LONG("long", Schema.Type.LONG),
        FLOAT("float", Schema.Type.FLOAT),
        DOUBLE("double", Schema.Type.DOUBLE),
        BOOLEAN("boolean", Schema.Type.BOOLEAN),
        ;

        private final String name;
        private final Schema.Type avroType;

        DevicePointType(String name, Schema.Type avroType) {
            this.name = name;
            this.avroType = avroType;
        }

        @JsonValue
        public String getName() {
            return name;
        }

        public Schema.Type avroType() {
            return avroType;
        }
    }
}
