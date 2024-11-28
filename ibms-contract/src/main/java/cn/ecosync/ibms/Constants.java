package cn.ecosync.ibms;

import jakarta.validation.groups.Default;

public class Constants {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public interface Create extends Default {
    }

    public interface Update extends Default {
    }
}
