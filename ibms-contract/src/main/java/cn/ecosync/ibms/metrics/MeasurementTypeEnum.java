package cn.ecosync.ibms.metrics;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum MeasurementTypeEnum {
    LONG(0, "long"),
    DOUBLE(1, "double"),
    ;

    final int i;
    private final String name;

    MeasurementTypeEnum(int i, String name) {
        this.i = i;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    private static final Map<String, MeasurementTypeEnum> MAP;

    static {
        MAP = Arrays.stream(MeasurementTypeEnum.values())
                .collect(Collectors.toMap(MeasurementTypeEnum::getName, e -> e));
    }

    public static MeasurementTypeEnum of(String name) {
        return MAP.get(name);
    }
}
