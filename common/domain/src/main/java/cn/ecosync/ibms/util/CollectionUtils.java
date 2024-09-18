package cn.ecosync.ibms.util;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CollectionUtils extends org.springframework.util.CollectionUtils {
    public static <T> Collection<T> nullSafeOf(Collection<T> collection) {
        return isEmpty(collection) ? Collections.emptyList() : collection;
    }

    public static <T> List<T> nullSafeOf(List<T> list) {
        return isEmpty(list) ? Collections.emptyList() : list;
    }

    public static <K, V> Map<K, V> nullSafeOf(@Nullable Map<K, V> map) {
        return isEmpty(map) ? Collections.emptyMap() : map;
    }

    public static boolean notEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean notEmpty(@Nullable Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static Pageable of(Integer page, Integer pageSize) {
        Pageable pageable;
        if (pageSize != null && page != null) {
            pageable = Pageable.ofSize(pageSize)
                    .withPage(page - 1);
        } else {
            pageable = Pageable.unpaged();
        }
        return pageable;
    }

    public static Map<String, Object> mapOf(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> map = CollectionUtils.newHashMap(2);
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }
}
