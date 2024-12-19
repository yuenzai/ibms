package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class ReadPropertyMultipleAck {
    private Integer deviceInstance;
    private List<BacnetObjectPropertiesValues> bopsValues;

    public List<BacnetObjectPropertiesValues> getBopsValues() {
        return CollectionUtils.nullSafeOf(bopsValues);
    }

    public boolean valuesNotEmpty() {
        return CollectionUtils.notEmpty(getBopsValues());
    }

    public MultiValueMap<BacnetObject, BacnetPropertyValues> toMultiValueMap() {
        MultiValueMap<BacnetObject, BacnetPropertyValues> multiValueMap = new LinkedMultiValueMap<>();
        // 遍历每个 BacnetObject
        for (BacnetObjectPropertiesValues bops : getBopsValues()) {
            for (BacnetPropertyValues propertyValues : bops.propertiesValues) {
                multiValueMap.add(bops.bacnetObject, propertyValues);
            }
        }
        return multiValueMap;
    }

    public static ReadPropertyMultipleAck nullInstance(Integer deviceInstance) {
        ReadPropertyMultipleAck nullInstance = new ReadPropertyMultipleAck();
        nullInstance.deviceInstance = deviceInstance;
        nullInstance.bopsValues = Collections.emptyList();
        return nullInstance;
    }

    @ToString
    public static class BacnetObjectPropertiesValues {
        @JsonUnwrapped
        private BacnetObject bacnetObject;
        @JsonAlias("properties")
        private List<BacnetPropertyValues> propertiesValues;
    }
}
