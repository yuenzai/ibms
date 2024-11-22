package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.iframework.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.validation.Valid;

@Getter
@ToString
public class BacnetReadPropertyMultipleQuery implements Query<ReadPropertyMultipleAck> {
    @Valid
    @JsonUnwrapped
    private BacnetReadPropertyMultipleService service;

    protected BacnetReadPropertyMultipleQuery() {
    }

    public BacnetReadPropertyMultipleQuery(BacnetReadPropertyMultipleService service) {
        Assert.notNull(service, "BacnetReadPropertyMultiple must not be null");
        this.service = service;
    }
}
