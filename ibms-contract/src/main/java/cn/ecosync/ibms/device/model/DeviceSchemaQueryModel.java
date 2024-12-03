package cn.ecosync.ibms.device.model;

import org.apache.avro.Schema;
import org.apache.avro.SchemaFormatter;

public interface DeviceSchemaQueryModel {
    DeviceSchemaId getDeviceSchemaId();

    DevicePoints getDevicePoints();

    Schema toAvroSchema();

    default String formatAvroSchema(Schema schema) {
        return SchemaFormatter.getInstance("json")
                .format(schema);
    }
}
