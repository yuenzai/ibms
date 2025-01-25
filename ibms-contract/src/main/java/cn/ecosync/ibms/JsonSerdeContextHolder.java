package cn.ecosync.ibms;

import cn.ecosync.iframework.serde.JsonSerde;

public class JsonSerdeContextHolder {
    private static final ThreadLocal<JsonSerde> JSON_SERDE_CONTEXT_HOLDER = new ThreadLocal<>();

    public static void set(JsonSerde jsonSerde) {
        JSON_SERDE_CONTEXT_HOLDER.set(jsonSerde);
    }

    public static JsonSerde get() {
        return JSON_SERDE_CONTEXT_HOLDER.get();
    }

    public static void clear() {
        JSON_SERDE_CONTEXT_HOLDER.remove();
    }
}
