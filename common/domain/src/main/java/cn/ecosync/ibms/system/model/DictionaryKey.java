package cn.ecosync.ibms.system.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@EqualsAndHashCode
public class DictionaryKey {
    @Column(name = "dict_key", nullable = false, updatable = false)
    private String dictKey;

    protected DictionaryKey() {
    }

    public DictionaryKey(String dictKey) {
        Assert.hasText(dictKey, "dictionary key must not be empty");
        this.dictKey = dictKey;
    }

    @Override
    public String toString() {
        return dictKey;
    }
}
