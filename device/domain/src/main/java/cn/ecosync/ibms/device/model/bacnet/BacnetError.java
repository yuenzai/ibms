package cn.ecosync.ibms.device.model.bacnet;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetError {
    private ClassEnum errorClass;
    private CodeEnum errorCode;

    @Getter
    @ToString
    public enum ClassEnum {
        ERROR_CLASS_DEVICE(0),
        ERROR_CLASS_OBJECT(1),
        ERROR_CLASS_PROPERTY(2),
        ERROR_CLASS_RESOURCES(3),
        ERROR_CLASS_SECURITY(4),
        ERROR_CLASS_SERVICES(5),
        ERROR_CLASS_VT(6),
        ERROR_CLASS_COMMUNICATION(7),
        ;

        @JsonValue
        private final int code;

        ClassEnum(int code) {
            this.code = code;
        }
    }

    @Getter
    @ToString
    public enum CodeEnum {
        /* valid for all classes */
        ERROR_CODE_OTHER(0),

        /* Error Class - Device */
        ERROR_CODE_DEVICE_BUSY(3),
        ERROR_CODE_CONFIGURATION_IN_PROGRESS(2),
        ERROR_CODE_OPERATIONAL_PROBLEM(25),

        /* Error Class - Object */
        ERROR_CODE_DYNAMIC_CREATION_NOT_SUPPORTED(4),
        ERROR_CODE_NO_OBJECTS_OF_SPECIFIED_TYPE(17),
        ERROR_CODE_OBJECT_DELETION_NOT_PERMITTED(23),
        ERROR_CODE_OBJECT_IDENTIFIER_ALREADY_EXISTS(24),
        ERROR_CODE_READ_ACCESS_DENIED(27),
        ERROR_CODE_UNKNOWN_OBJECT(31),
        ERROR_CODE_UNSUPPORTED_OBJECT_TYPE(36),

        /* Error Class - Property */
        ERROR_CODE_CHARACTER_SET_NOT_SUPPORTED(41),
        ERROR_CODE_DATATYPE_NOT_SUPPORTED(47),
        ERROR_CODE_INCONSISTENT_SELECTION_CRITERION(8),
        ERROR_CODE_INVALID_ARRAY_INDEX(42),
        ERROR_CODE_INVALID_DATA_TYPE(9),
        ERROR_CODE_NOT_COV_PROPERTY(44),
        ERROR_CODE_OPTIONAL_FUNCTIONALITY_NOT_SUPPORTED(45),
        ERROR_CODE_PROPERTY_IS_NOT_AN_ARRAY(50),
        /* ERROR_CODE_READ_ACCESS_DENIED(27), */
        ERROR_CODE_UNKNOWN_PROPERTY(32),
        ERROR_CODE_VALUE_OUT_OF_RANGE(37),
        ERROR_CODE_WRITE_ACCESS_DENIED(40),

        /* Error Class - Resources */
        ERROR_CODE_NO_SPACE_FOR_OBJECT(18),
        ERROR_CODE_NO_SPACE_TO_ADD_LIST_ELEMENT(19),
        ERROR_CODE_NO_SPACE_TO_WRITE_PROPERTY(20),

        /* Error Class - Security */
        ERROR_CODE_AUTHENTICATION_FAILED(1),
        /* ERROR_CODE_CHARACTER_SET_NOT_SUPPORTED(41), */
        ERROR_CODE_INCOMPATIBLE_SECURITY_LEVELS(6),
        ERROR_CODE_INVALID_OPERATOR_NAME(12),
        ERROR_CODE_KEY_GENERATION_ERROR(15),
        ERROR_CODE_PASSWORD_FAILURE(26),
        ERROR_CODE_SECURITY_NOT_SUPPORTED(28),
        ERROR_CODE_TIMEOUT(30),

        /* Error Class - Services */
        /* ERROR_CODE_CHARACTER_SET_NOT_SUPPORTED(41), */
        ERROR_CODE_COV_SUBSCRIPTION_FAILED(43),
        ERROR_CODE_DUPLICATE_NAME(48),
        ERROR_CODE_DUPLICATE_OBJECT_ID(49),
        ERROR_CODE_FILE_ACCESS_DENIED(5),
        ERROR_CODE_INCONSISTENT_PARAMETERS(7),
        ERROR_CODE_INVALID_CONFIGURATION_DATA(46),
        ERROR_CODE_INVALID_FILE_ACCESS_METHOD(10),
        ERROR_CODE_INVALID_FILE_START_POSITION(11),
        ERROR_CODE_INVALID_PARAMETER_DATA_TYPE(13),
        ERROR_CODE_INVALID_TIME_STAMP(14),
        ERROR_CODE_MISSING_REQUIRED_PARAMETER(16),
        /* ERROR_CODE_OPTIONAL_FUNCTIONALITY_NOT_SUPPORTED(45), */
        ERROR_CODE_PROPERTY_IS_NOT_A_LIST(22),
        ERROR_CODE_SERVICE_REQUEST_DENIED(29),

        /* Error Class - VT */
        ERROR_CODE_UNKNOWN_VT_CLASS(34),
        ERROR_CODE_UNKNOWN_VT_SESSION(35),
        ERROR_CODE_NO_VT_SESSIONS_AVAILABLE(21),
        ERROR_CODE_VT_SESSION_ALREADY_CLOSED(38),
        ERROR_CODE_VT_SESSION_TERMINATION_FAILURE(39),

        /* unused */
        ERROR_CODE_RESERVED1(33),
        /* new error codes from new addenda */
        ERROR_CODE_ABORT_BUFFER_OVERFLOW(51),
        ERROR_CODE_ABORT_INVALID_APDU_IN_THIS_STATE(52),
        ERROR_CODE_ABORT_PREEMPTED_BY_HIGHER_PRIORITY_TASK(53),
        ERROR_CODE_ABORT_SEGMENTATION_NOT_SUPPORTED(54),
        ERROR_CODE_ABORT_PROPRIETARY(55),
        ERROR_CODE_ABORT_OTHER(56),
        ERROR_CODE_INVALID_TAG(57),
        ERROR_CODE_NETWORK_DOWN(58),
        ERROR_CODE_REJECT_BUFFER_OVERFLOW(59),
        ERROR_CODE_REJECT_INCONSISTENT_PARAMETERS(60),
        ERROR_CODE_REJECT_INVALID_PARAMETER_DATA_TYPE(61),
        ERROR_CODE_REJECT_INVALID_TAG(62),
        ERROR_CODE_REJECT_MISSING_REQUIRED_PARAMETER(63),
        ERROR_CODE_REJECT_PARAMETER_OUT_OF_RANGE(64),
        ERROR_CODE_REJECT_TOO_MANY_ARGUMENTS(65),
        ERROR_CODE_REJECT_UNDEFINED_ENUMERATION(66),
        ERROR_CODE_REJECT_UNRECOGNIZED_SERVICE(67),
        ERROR_CODE_REJECT_PROPRIETARY(68),
        ERROR_CODE_REJECT_OTHER(69),
        ERROR_CODE_UNKNOWN_DEVICE(70),
        ERROR_CODE_UNKNOWN_ROUTE(71),
        ERROR_CODE_VALUE_NOT_INITIALIZED(72),
        ERROR_CODE_INVALID_EVENT_STATE(73),
        ERROR_CODE_NO_ALARM_CONFIGURED(74),
        ERROR_CODE_LOG_BUFFER_FULL(75),
        ERROR_CODE_LOGGED_VALUE_PURGED(76),
        ERROR_CODE_NO_PROPERTY_SPECIFIED(77),
        ERROR_CODE_NOT_CONFIGURED_FOR_TRIGGERED_LOGGING(78),
        ERROR_CODE_UNKNOWN_SUBSCRIPTION(79),
        ERROR_CODE_PARAMETER_OUT_OF_RANGE(80),
        ERROR_CODE_LIST_ELEMENT_NOT_FOUND(81),
        ERROR_CODE_BUSY(82),
        ERROR_CODE_COMMUNICATION_DISABLED(83),
        ERROR_CODE_SUCCESS(84),
        ERROR_CODE_ACCESS_DENIED(85),
        ERROR_CODE_BAD_DESTINATION_ADDRESS(86),
        ERROR_CODE_BAD_DESTINATION_DEVICE_ID(87),
        ERROR_CODE_BAD_SIGNATURE(88),
        ERROR_CODE_BAD_SOURCE_ADDRESS(89),
        ERROR_CODE_BAD_TIMESTAMP(90),
        ERROR_CODE_CANNOT_USE_KEY(91),
        ERROR_CODE_CANNOT_VERIFY_MESSAGE_ID(92),
        ERROR_CODE_CORRECT_KEY_REVISION(93),
        ERROR_CODE_DESTINATION_DEVICE_ID_REQUIRED(94),
        ERROR_CODE_DUPLICATE_MESSAGE(95),
        ERROR_CODE_ENCRYPTION_NOT_CONFIGURED(96),
        ERROR_CODE_ENCRYPTION_REQUIRED(97),
        ERROR_CODE_INCORRECT_KEY(98),
        ERROR_CODE_INVALID_KEY_DATA(99),
        ERROR_CODE_KEY_UPDATE_IN_PROGRESS(100),
        ERROR_CODE_MALFORMED_MESSAGE(101),
        ERROR_CODE_NOT_KEY_SERVER(102),
        ERROR_CODE_SECURITY_NOT_CONFIGURED(103),
        ERROR_CODE_SOURCE_SECURITY_REQUIRED(104),
        ERROR_CODE_TOO_MANY_KEYS(105),
        ERROR_CODE_UNKNOWN_AUTHENTICATION_TYPE(106),
        ERROR_CODE_UNKNOWN_KEY(107),
        ERROR_CODE_UNKNOWN_KEY_REVISION(108),
        ERROR_CODE_UNKNOWN_SOURCE_MESSAGE(109),
        ERROR_CODE_NOT_ROUTER_TO_DNET(110),
        ERROR_CODE_ROUTER_BUSY(111),
        ERROR_CODE_UNKNOWN_NETWORK_MESSAGE(112),
        ERROR_CODE_MESSAGE_TOO_LONG(113),
        ERROR_CODE_SECURITY_ERROR(114),
        ERROR_CODE_ADDRESSING_ERROR(115),
        ERROR_CODE_WRITE_BDT_FAILED(116),
        ERROR_CODE_READ_BDT_FAILED(117),
        ERROR_CODE_REGISTER_FOREIGN_DEVICE_FAILED(118),
        ERROR_CODE_READ_FDT_FAILED(119),
        ERROR_CODE_DELETE_FDT_ENTRY_FAILED(120),
        ERROR_CODE_DISTRIBUTE_BROADCAST_FAILED(121),
        ERROR_CODE_UNKNOWN_FILE_SIZE(122),
        ERROR_CODE_ABORT_APDU_TOO_LONG(123),
        ERROR_CODE_ABORT_APPLICATION_EXCEEDED_REPLY_TIME(124),
        ERROR_CODE_ABORT_OUT_OF_RESOURCES(125),
        ERROR_CODE_ABORT_TSM_TIMEOUT(126),
        ERROR_CODE_ABORT_WINDOW_SIZE_OUT_OF_RANGE(127),
        ERROR_CODE_FILE_FULL(128),
        ERROR_CODE_INCONSISTENT_CONFIGURATION(129),
        ERROR_CODE_INCONSISTENT_OBJECT_TYPE(130),
        ERROR_CODE_INTERNAL_ERROR(131),
        ERROR_CODE_NOT_CONFIGURED(132),
        ERROR_CODE_OUT_OF_MEMORY(133),
        ERROR_CODE_VALUE_TOO_LONG(134),
        ERROR_CODE_ABORT_INSUFFICIENT_SECURITY(135),
        ERROR_CODE_ABORT_SECURITY_ERROR(136),
        ERROR_CODE_DUPLICATE_ENTRY(137),
        ERROR_CODE_INVALID_VALUE_IN_THIS_STATE(138),
        ERROR_CODE_INVALID_OPERATION_IN_THIS_STATE(139),
        ERROR_CODE_LIST_ITEM_NOT_NUMBERED(140),
        ERROR_CODE_LIST_ITEM_NOT_TIMESTAMPED(141),
        ERROR_CODE_INVALID_DATA_ENCODING(142),
        ERROR_CODE_BVLC_FUNCTION_UNKNOWN(143),
        ERROR_CODE_BVLC_PROPRIETARY_FUNCTION_UNKNOWN(144),
        ERROR_CODE_HEADER_ENCODING_ERROR(145),
        ERROR_CODE_HEADER_NOT_UNDERSTOOD(146),
        ERROR_CODE_MESSAGE_INCOMPLETE(147),
        ERROR_CODE_NOT_A_BACNET_SC_HUB(148),
        ERROR_CODE_PAYLOAD_EXPECTED(149),
        ERROR_CODE_UNEXPECTED_DATA(150),
        ERROR_CODE_NODE_DUPLICATE_VMAC(151),
        ERROR_CODE_HTTP_UNEXPECTED_RESPONSE_CODE(152),
        ERROR_CODE_HTTP_NO_UPGRADE(153),
        ERROR_CODE_HTTP_RESOURCE_NOT_LOCAL(154),
        ERROR_CODE_HTTP_PROXY_AUTHENTICATION_FAILED(155),
        ERROR_CODE_HTTP_RESPONSE_TIMEOUT(156),
        ERROR_CODE_HTTP_RESPONSE_SYNTAX_ERROR(157),
        ERROR_CODE_HTTP_RESPONSE_VALUE_ERROR(158),
        ERROR_CODE_HTTP_RESPONSE_MISSING_HEADER(159),
        ERROR_CODE_HTTP_WEBSOCKET_HEADER_ERROR(160),
        ERROR_CODE_HTTP_UPGRADE_REQUIRED(161),
        ERROR_CODE_HTTP_UPGRADE_ERROR(162),
        ERROR_CODE_HTTP_TEMPORARY_UNAVAILABLE(163),
        ERROR_CODE_HTTP_NOT_A_SERVER(164),
        ERROR_CODE_HTTP_ERROR(165),
        ERROR_CODE_WEBSOCKET_SCHEME_NOT_SUPPORTED(166),
        ERROR_CODE_WEBSOCKET_UNKNOWN_CONTROL_MESSAGE(167),
        ERROR_CODE_WEBSOCKET_CLOSE_ERROR(168),
        ERROR_CODE_WEBSOCKET_CLOSED_BY_PEER(169),
        ERROR_CODE_WEBSOCKET_ENDPOINT_LEAVES(170),
        ERROR_CODE_WEBSOCKET_PROTOCOL_ERROR(171),
        ERROR_CODE_WEBSOCKET_DATA_NOT_ACCEPTED(172),
        ERROR_CODE_WEBSOCKET_CLOSED_ABNORMALLY(173),
        ERROR_CODE_WEBSOCKET_DATA_INCONSISTENT(174),
        ERROR_CODE_WEBSOCKET_DATA_AGAINST_POLICY(175),
        ERROR_CODE_WEBSOCKET_FRAME_TOO_LONG(176),
        ERROR_CODE_WEBSOCKET_EXTENSION_MISSING(177),
        ERROR_CODE_WEBSOCKET_REQUEST_UNAVAILABLE(178),
        ERROR_CODE_WEBSOCKET_ERROR(179),
        ERROR_CODE_TLS_CLIENT_CERTIFICATE_ERROR(180),
        ERROR_CODE_TLS_SERVER_CERTIFICATE_ERROR(181),
        ERROR_CODE_TLS_CLIENT_AUTHENTICATION_FAILED(182),
        ERROR_CODE_TLS_SERVER_AUTHENTICATION_FAILED(183),
        ERROR_CODE_TLS_CLIENT_CERTIFICATE_EXPIRED(184),
        ERROR_CODE_TLS_SERVER_CERTIFICATE_EXPIRED(185),
        ERROR_CODE_TLS_CLIENT_CERTIFICATE_REVOKED(186),
        ERROR_CODE_TLS_SERVER_CERTIFICATE_REVOKED(187),
        ERROR_CODE_TLS_ERROR(188),
        ERROR_CODE_DNS_UNAVAILABLE(189),
        ERROR_CODE_DNS_NAME_RESOLUTION_FAILED(190),
        ERROR_CODE_DNS_RESOLVER_FAILURE(191),
        ERROR_CODE_DNS_ERROR(192),
        ERROR_CODE_TCP_CONNECT_TIMEOUT(193),
        ERROR_CODE_TCP_CONNECTION_REFUSED(194),
        ERROR_CODE_TCP_CLOSED_BY_LOCAL(195),
        ERROR_CODE_TCP_CLOSED_OTHER(196),
        ERROR_CODE_TCP_ERROR(197),
        ERROR_CODE_IP_ADDRESS_NOT_REACHABLE(198),
        ERROR_CODE_IP_ERROR(199),
        ERROR_CODE_CERTIFICATE_EXPIRED(200),
        ERROR_CODE_CERTIFICATE_INVALID(201),
        ERROR_CODE_CERTIFICATE_MALFORMED(202),
        ERROR_CODE_CERTIFICATE_REVOKED(203),
        ERROR_CODE_UNKNOWN_SECURITY_KEY(204),
        ERROR_CODE_REFERENCED_PORT_IN_ERROR(205),
        ;

        @JsonValue
        private final int code;

        CodeEnum(int code) {
            this.code = code;
        }
    }
}