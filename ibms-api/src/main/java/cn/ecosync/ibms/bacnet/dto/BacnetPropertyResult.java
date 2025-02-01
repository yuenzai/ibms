package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class BacnetPropertyResult {
    @JsonUnwrapped
    private BacnetProperty property;
    private BacnetPropertyValue value;
    private List<BacnetPropertyValue> values;
    private BacnetError error;

    public List<BacnetPropertyValue> getValues() {
        return CollectionUtils.nullSafeOf(values);
    }
}
