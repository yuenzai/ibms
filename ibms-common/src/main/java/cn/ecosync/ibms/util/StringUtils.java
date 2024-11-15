package cn.ecosync.ibms.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
public class StringUtils extends org.springframework.util.StringUtils {
    public static String nullSafeOf(String text) {
        return hasText(text) ? text : "";
    }

    public static Optional<BigDecimal> stringToDecimal(String text) {
        try {
            return Optional.of(new BigDecimal(text));
        } catch (Exception e) {
            log.error("", e);
        }
        return Optional.empty();
    }
}
