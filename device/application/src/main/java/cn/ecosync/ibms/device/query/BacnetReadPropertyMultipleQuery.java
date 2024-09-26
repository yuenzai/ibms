package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.bacnet.BacnetObject;
import cn.ecosync.ibms.device.model.bacnet.BacnetProperty;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import cn.ecosync.ibms.query.Query;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class BacnetReadPropertyMultipleQuery implements Query<List<ReadPropertyMultipleAck>> {
    @NotNull
    private Integer deviceInstance;
    @NotEmpty
    private List<BacnetObjectProperties> objectProperties;

    protected BacnetReadPropertyMultipleQuery() {
    }

    public BacnetReadPropertyMultipleQuery(Integer deviceInstance, Map<BacnetObject, List<BacnetProperty>> objectProperties) {
        this(deviceInstance, objectProperties.entrySet().stream()
                .map(in -> new BacnetObjectProperties(in.getKey(), in.getValue()))
                .collect(Collectors.toList()));
    }

    public BacnetReadPropertyMultipleQuery(Integer deviceInstance, List<BacnetObjectProperties> objectProperties) {
        this.deviceInstance = deviceInstance;
        this.objectProperties = objectProperties;
    }

    @Getter
    @ToString
    public static class BacnetObjectProperties extends BacnetObject {
        @NotEmpty
        private List<BacnetProperty> properties;

        protected BacnetObjectProperties() {
        }

        public BacnetObjectProperties(BacnetObject bacnetObject, List<BacnetProperty> properties) {
            super(bacnetObject.getObjectType(), bacnetObject.getObjectInstance());
            this.properties = properties;
        }
    }
}
