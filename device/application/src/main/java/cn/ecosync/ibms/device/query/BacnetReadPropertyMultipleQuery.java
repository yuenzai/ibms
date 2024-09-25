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

@Getter
@ToString
public class BacnetReadPropertyMultipleQuery implements Query<List<ReadPropertyMultipleAck>> {
    @NotNull
    private Integer deviceInstance;
    @NotEmpty
    private List<BacnetObjectProperties> objectProperties;

    @Getter
    @ToString
    public static class BacnetObjectProperties extends BacnetObject {
        @NotEmpty
        private List<BacnetProperty> properties;
    }
}
