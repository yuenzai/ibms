package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.util.CollectionUtils;
import cn.ecosync.iframework.util.StringUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.apache.iceberg.types.Type.PrimitiveType;
import org.apache.iceberg.types.Types;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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

    public List<DevicePoint> getDevicePoints() {
        return Collections.unmodifiableList(this.devicePoints);
    }

    @Override
    public Iterator<DevicePoint> iterator() {
        return getDevicePoints().iterator();
    }

    @Getter
    @ToString
    public static class DevicePoint {
        @NotBlank
        private String pointCode;
        @NotNull
        private Boolean optional;
        @NotNull
        private String pointType;
        @Valid
        @NotNull
        private DevicePointProperties pointExtra;
        private String pointName;

        protected DevicePoint() {
        }

        public DevicePoint(String pointCode, Boolean optional, String pointType, DevicePointProperties pointExtra, String pointName) {
            Assert.hasText(pointCode, "pointCode can not be empty");
            Assert.notNull(optional, "optional can not be null");
            Types.fromPrimitiveString(pointType);
            Assert.notNull(pointExtra, "pointExtra can not be null");
            this.pointCode = pointCode;
            this.optional = optional;
            this.pointType = pointType;
            this.pointExtra = pointExtra;
            this.pointName = StringUtils.nullSafeOf(pointName);
        }

        public PrimitiveType toIcebergType() {
            return Types.fromPrimitiveString(pointType);
        }
    }
//    @ToString
//    public enum DevicePointType {
//        BOOLEAN(BooleanType.get().toString(), BooleanType.get()),
//        INTEGER(IntegerType.get().toString(), IntegerType.get()),
//        LONG(LongType.get().toString(), LongType.get()),
//        FLOAT(FloatType.get().toString(), FloatType.get()),
//        DOUBLE(DoubleType.get().toString(), DoubleType.get()),
//        DATE(DateType.get().toString(), DateType.get()),
//        TIME(TimeType.get().toString(), TimeType.get()),
//        TIMESTAMP_TZ(TimestampType.withZone().toString(), TimestampType.withZone()),
//        TIMESTAMP(TimestampType.withoutZone().toString(), TimestampType.withoutZone()),
//        TIMESTAMP_NANO_TZ(TimestampNanoType.withZone().toString(), TimestampNanoType.withZone()),
//        TIMESTAMP_NANO(TimestampNanoType.withoutZone().toString(), TimestampNanoType.withoutZone()),
//        STRING(StringType.get().toString(), StringType.get()),
//        UUID(UUIDType.get().toString(), UUIDType.get()),
//        BINARY(BinaryType.get().toString(), BinaryType.get()),
//        ;
//
//        private final String typeString;
//        private final Type type;
//
//        DevicePointType(String typeString, Type type) {
//            this.typeString = typeString;
//            this.type = type;
//        }
//
//        @JsonValue
//        public String getTypeString() {
//            return typeString;
//        }
//    }
}
