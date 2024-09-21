package cn.ecosync.ibms.system.model;

import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import org.springframework.util.Assert;

public class Dictionary extends ConcurrencySafeEntity implements AggregateRoot {
    private DictionaryKey key;

    private DictionaryValue value;

    protected Dictionary() {
    }

    public Dictionary(DictionaryKey key, DictionaryValue value) {
        Assert.notNull(key, "dictionary key must not be null");
        Assert.notNull(value, "dictionary value must not be null");
        this.key = key;
        this.value = value;
    }

    public void setValue(DictionaryValue value) {
        if (value != null) {
            this.value = value;
        }
    }

    @Override
    public String aggregateType() {
        return DictionaryConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return key.toString();
    }
}
