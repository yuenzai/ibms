package cn.ecosync.ibms.bacnet.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString
public enum BacnetObjectType {
    ANALOG_INPUT(0),
    ANALOG_OUTPUT(1),
    ANALOG_VALUE(2),
    BINARY_INPUT(3),
    BINARY_OUTPUT(4),
    BINARY_VALUE(5),
    CALENDAR(6),
    COMMAND(7),
    DEVICE(8),
    EVENT_ENROLLMENT(9),
    FILE(10),
    GROUP(11),
    LOOP(12),
    MULTI_STATE_INPUT(13),
    MULTI_STATE_OUTPUT(14),
    NOTIFICATION_CLASS(15),
    PROGRAM(16),
    SCHEDULE(17),
    AVERAGING(18),
    MULTI_STATE_VALUE(19),
    TRENDLOG(20),
    LIFE_SAFETY_POINT(21),
    LIFE_SAFETY_ZONE(22),
    ACCUMULATOR(23),
    PULSE_CONVERTER(24),
    EVENT_LOG(25),
    GLOBAL_GROUP(26),
    TREND_LOG_MULTIPLE(27),
    LOAD_CONTROL(28),
    STRUCTURED_VIEW(29),
    ACCESS_DOOR(30),
    TIMER(31),/* Note: 31 was lighting output, but BACnet editor changed it... */
    ACCESS_CREDENTIAL(32), /* Addendum 2008-j */
    ACCESS_POINT(33),
    ACCESS_RIGHTS(34),
    ACCESS_USER(35),
    ACCESS_ZONE(36),
    CREDENTIAL_DATA_INPUT(37), /* authentication-factor-input */
    NETWORK_SECURITY(38), /* Addendum 2008-g */
    BITSTRING_VALUE(39), /* Addendum 2008-w */
    CHARACTERSTRING_VALUE(40), /* Addendum 2008-w */
    DATE_PATTERN_VALUE(41), /* Addendum 2008-w */
    DATE_VALUE(42), /* Addendum 2008-w */
    DATETIME_PATTERN_VALUE(43), /* Addendum 2008-w */
    DATETIME_VALUE(44), /* Addendum 2008-w */
    INTEGER_VALUE(45), /* Addendum 2008-w */
    LARGE_ANALOG_VALUE(46), /* Addendum 2008-w */
    OCTETSTRING_VALUE(47), /* Addendum 2008-w */
    POSITIVE_INTEGER_VALUE(48), /* Addendum 2008-w */
    TIME_PATTERN_VALUE(49), /* Addendum 2008-w */
    TIME_VALUE(50), /* Addendum 2008-w */
    NOTIFICATION_FORWARDER(51), /* Addendum 2010-af */
    ALERT_ENROLLMENT(52), /* Addendum 2010-af */
    CHANNEL(53), /* Addendum 2010-aa */
    LIGHTING_OUTPUT(54), /* Addendum 2010-i */
    BINARY_LIGHTING_OUTPUT(55), /* Addendum 135-2012az */
    NETWORK_PORT(56), /* Addendum 135-2012az */
    ELEVATOR_GROUP(57), /* Addendum 135-2012aq */
    ESCALATOR(58), /* Addendum 135-2012aq */
    LIFT(59), /* Addendum 135-2012aq */
    STAGING(60), /* Addendum 135-2016bd */
    AUDIT_LOG(61), /* Addendum 135-2016bi */
    AUDIT_REPORTER(62), /* Addendum 135-2016bi */
    COLOR(63), /* Addendum 135-2020ca */
    COLOR_TEMPERATURE(64), /* Addendum 135-2020ca */;

    private final Integer code;

    BacnetObjectType(Integer code) {
        this.code = code;
    }

    private static final Map<Integer, BacnetObjectType> MAP = Arrays.stream(values())
            .collect(Collectors.toMap(BacnetObjectType::getCode, Function.identity()));

    public static BacnetObjectType of(Integer code) {
        return MAP.get(code);
    }
}
