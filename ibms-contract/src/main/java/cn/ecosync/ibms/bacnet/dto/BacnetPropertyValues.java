package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionDelegate;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@ToString
public class BacnetPropertyValues implements CollectionDelegate<BacnetPropertyValue> {
    @Getter
    @JsonUnwrapped
    private BacnetProperty property;
    private List<BacnetPropertyValue> propertyValues;
    @Getter
    private BacnetError error;

    @Override
    public Collection<BacnetPropertyValue> delegate() {
        return propertyValues;
    }
}
