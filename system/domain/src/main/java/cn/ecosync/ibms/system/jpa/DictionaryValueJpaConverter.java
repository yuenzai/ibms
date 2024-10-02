package cn.ecosync.ibms.system.jpa;

import cn.ecosync.ibms.jpa.PropertiesJpaConverter;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.system.model.DictionaryValue;

import javax.persistence.Converter;

@Converter
public class DictionaryValueJpaConverter extends PropertiesJpaConverter<DictionaryValue> {
    public DictionaryValueJpaConverter() {
    }

    public DictionaryValueJpaConverter(JsonSerde jsonSerde) {
        super(DictionaryValue.class, jsonSerde);
    }
}
