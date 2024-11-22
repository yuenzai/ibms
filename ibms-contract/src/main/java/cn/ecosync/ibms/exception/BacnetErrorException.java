package cn.ecosync.ibms.exception;

import cn.ecosync.ibms.model.BacnetError;
import lombok.Getter;

@Getter
public class BacnetErrorException extends RuntimeException {
    private final BacnetError bacnetError;

    public BacnetErrorException(BacnetError bacnetError) {
        this.bacnetError = bacnetError;
    }
}
