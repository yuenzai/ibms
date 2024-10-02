package cn.ecosync.ibms.system.model;

import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import cn.ecosync.ibms.system.jpa.DictionaryValueJpaConverter;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Table(name = "dictionary")
@Getter
public class Dictionary extends ConcurrencySafeEntity implements AggregateRoot {
    @Embedded
    private DictionaryKey key;
    @Convert(converter = DictionaryValueJpaConverter.class)
    @Column(name = "dict_value", nullable = false)
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
