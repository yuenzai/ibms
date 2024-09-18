package cn.ecosync.ibms.system.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class SystemDictionaryValue {
    private Map<String, Object> dictValue;

    protected SystemDictionaryValue() {
    }

    public SystemDictionaryValue(Map<String, Object> dictValue) {
        Assert.notNull(dictValue, "dictionary value must not be null");
        this.dictValue = dictValue;
    }

    public Map<String, Object> getDictValue() {
        return Collections.unmodifiableMap(dictValue);
    }
}
