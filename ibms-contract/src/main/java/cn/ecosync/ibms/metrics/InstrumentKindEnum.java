package cn.ecosync.ibms.metrics;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum InstrumentKindEnum {
    COUNTER(0, "counter"),
    UP_DOWN_COUNTER(2, "up-down-counter"),
    GAUGE(4, "gauge"),
    HISTOGRAM(6, "histogram"),
    ;

    final int i;
    private final String name;

    InstrumentKindEnum(int i, String name) {
        this.i = i;
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    private static final Map<String, InstrumentKindEnum> MAP;

    static {
        MAP = Arrays.stream(InstrumentKindEnum.values())
                .collect(Collectors.toMap(InstrumentKindEnum::getName, e -> e));
    }

    public static InstrumentKindEnum of(String name) {
        return MAP.get(name);
    }
}
