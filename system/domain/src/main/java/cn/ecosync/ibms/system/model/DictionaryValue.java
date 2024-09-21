package cn.ecosync.ibms.system.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class DictionaryValue {
    private Map<String, Object> dictValue;

    protected DictionaryValue() {
    }

    public DictionaryValue(Map<String, Object> dictValue) {
        Assert.notNull(dictValue, "dictionary value must not be null");
        this.dictValue = dictValue;
    }

    public Map<String, Object> getDictValue() {
        return Collections.unmodifiableMap(dictValue);
    }
}
