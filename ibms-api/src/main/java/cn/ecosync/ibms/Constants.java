package cn.ecosync.ibms;

import java.util.regex.Pattern;

public class Constants {
    public static final String REGEX_CODE = "[a-zA-Z_]\\w*";
    public static final Pattern PATTERN_CODE = Pattern.compile(REGEX_CODE);
}
