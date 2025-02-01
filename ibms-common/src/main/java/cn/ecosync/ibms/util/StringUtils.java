package cn.ecosync.ibms.util;

/**
 * @author yuenzai
 * @since 2024
 */
public class StringUtils extends org.springframework.util.StringUtils {
    public static String nullSafeOf(String text) {
        return hasText(text) ? text : "";
    }
}
