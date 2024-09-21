package cn.ecosync.ibms.system.query;

import cn.ecosync.ibms.query.Query;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class GetDictionaryQuery implements Query<Map<String, Object>> {
    private final String dictKey;
}
