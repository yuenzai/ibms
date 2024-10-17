package cn.ecosync.ibms.bacnet.exception;

import cn.ecosync.ibms.bacnet.model.BacnetError;
import lombok.Getter;

@Getter
public class BacnetErrorException extends RuntimeException {
    private final BacnetError bacnetError;

    public BacnetErrorException(BacnetError bacnetError) {
        this.bacnetError = bacnetError;
    }
}
