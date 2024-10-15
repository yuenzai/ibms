package cn.ecosync.ibms.bacnet.controller;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.model.*;
import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService.BacnetObjectProperties;
import cn.ecosync.ibms.bacnet.service.BacnetApplicationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/" + BacnetConstants.BACNET)
public class BacnetRestController implements ApplicationRunner {
    private final ObjectMapper jsonSerde;
    private final File DATA_DIR = new File(System.getProperty("java.io.tmpdir") + "/bacnet");

    @PostMapping("/readpropm")
    public List<ReadPropertyMultipleAck> readpropm(@RequestBody @Validated BacnetReadPropertyMultipleService service) {
        try {
            String stdout = BacnetApplicationService.readPropertyMultiple(service);
            return readValue(jsonSerde, stdout, new TypeReference<List<ReadPropertyMultipleAck>>() {
            }).orElse(Collections.emptyList());
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }

    @PostMapping("/readpropmToFile")
    public void readpropmToFile(@RequestBody @Validated BacnetReadPropertyMultipleService service) {
        try {
            int exitCode = BacnetApplicationService.readPropertyMultiple(service, DATA_DIR);
            if (exitCode != 0) {
                log.error("readpropm exit code: {}", exitCode);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @GetMapping("/device/{deviceInstance}/object-ids")
    public List<BacnetObject> getDeviceObjectIds(@PathVariable Integer deviceInstance) {
        BacnetObject deviceObject = new BacnetObject(BacnetObjectType.OBJECT_DEVICE, deviceInstance);
        BacnetProperty objectIdsProperty = new BacnetProperty(BacnetPropertyId.PROP_OBJECT_LIST, null);
        BacnetObjectProperties objectProperties = new BacnetObjectProperties(deviceObject, Collections.singletonList(objectIdsProperty));
        BacnetReadPropertyMultipleService service = new BacnetReadPropertyMultipleService(deviceInstance, Collections.singletonList(objectProperties));
        try {
            String stdout = BacnetApplicationService.readPropertyMultiple(service);
            List<ReadPropertyMultipleAck> acks = readValue(jsonSerde, stdout, new TypeReference<List<ReadPropertyMultipleAck>>() {
            }).orElse(Collections.emptyList());
            ReadPropertyMultipleAck ack = CollectionUtils.firstElement(acks);
            if (ack == null) {
                return Collections.emptyList();
            }
            ReadPropertyMultipleAck.Property objectIdsPropertyValue = CollectionUtils.firstElement(ack.getProperties());
            if (objectIdsPropertyValue == null) {
                return Collections.emptyList();
            }
            BacnetError bacnetError = objectIdsPropertyValue.getError().orElse(null);
            if (bacnetError != null) {
                log.info("find bacnet error: {}", bacnetError);
                return Collections.emptyList();
            }
            return objectIdsPropertyValue.getPropertyValues().stream()
                    .filter(in -> in instanceof BacnetPropertyValue.OBJECT_ID)
                    .map(in -> ((BacnetPropertyValue.OBJECT_ID) in).getValue())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }

    private static <T> Optional<T> readValue(ObjectMapper objectMapper, String json, TypeReference<T> valueTypeRef) {
        try {
            return Optional.ofNullable(objectMapper.readValue(json, valueTypeRef));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!DATA_DIR.exists()) {
            DATA_DIR.mkdirs();
        }
    }
}
