package cn.ecosync.ibms.system.query;

import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.model.DictionaryValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
@RequiredArgsConstructor
public class GetDictionaryQuery implements Query<Optional<DictionaryValue>> {
    private final DictionaryKey dictKey;
}
