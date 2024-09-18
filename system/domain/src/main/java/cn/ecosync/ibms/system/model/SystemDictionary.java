package cn.ecosync.ibms.system.model;

import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import org.springframework.util.Assert;

public class SystemDictionary extends ConcurrencySafeEntity implements AggregateRoot {
    private SystemDictionaryKey key;

    private SystemDictionaryValue value;

    protected SystemDictionary() {
    }

    public SystemDictionary(SystemDictionaryKey key, SystemDictionaryValue value) {
        Assert.notNull(key, "dictionary key must not be null");
        Assert.notNull(value, "dictionary value must not be null");
        this.key = key;
        this.value = value;
    }

    public void setValue(SystemDictionaryValue value) {
        if (value != null) {
            this.value = value;
        }
    }

    @Override
    public String aggregateType() {
        return SystemDictionaryConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return key.toString();
    }
}
