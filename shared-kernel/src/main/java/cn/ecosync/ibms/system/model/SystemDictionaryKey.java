package cn.ecosync.ibms.system.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@EqualsAndHashCode
public class SystemDictionaryKey {
    private String dictKey;

    protected SystemDictionaryKey() {
    }

    public SystemDictionaryKey(String dictKey) {
        Assert.hasText(dictKey, "dictionary key must not be empty");
        this.dictKey = dictKey;
    }

    @Override
    public String toString() {
        return dictKey;
    }
}
