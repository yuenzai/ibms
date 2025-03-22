CREATE TABLE gateway_data_acquisition
(
    id                    SERIAL PRIMARY KEY NOT NULL,
    data_acquisition_code VARCHAR(64)        NOT NULL,
    payload               TEXT               NOT NULL,
    version               INTEGER            NOT NULL,
    created_date          BIGINT             NOT NULL,
    last_modified_date    BIGINT             NOT NULL,
    CONSTRAINT uk_gateway_data_acquisition UNIQUE (data_acquisition_code)
);

COMMENT ON TABLE gateway_data_acquisition IS '设备数据采集';
COMMENT ON COLUMN gateway_data_acquisition.id IS '主键';
COMMENT ON COLUMN gateway_data_acquisition.data_acquisition_code IS '设备数据采集编码';
COMMENT ON COLUMN gateway_data_acquisition.version IS '乐观锁版本';
COMMENT ON COLUMN gateway_data_acquisition.created_date IS '创建时间';
COMMENT ON COLUMN gateway_data_acquisition.last_modified_date IS '修改时间';

CREATE TABLE gateway_data_point
(
    id                  SERIAL PRIMARY KEY NOT NULL,
    data_acquisition_id INTEGER            NOT NULL,
    device_code         VARCHAR(64)        NOT NULL,
    metric_name         VARCHAR(64)        NOT NULL,
    CONSTRAINT uk_gateway_data_point UNIQUE (device_code, metric_name)
);

COMMENT ON TABLE gateway_data_point IS '设备数据点位';
COMMENT ON COLUMN gateway_data_point.id IS '主键';
COMMENT ON COLUMN gateway_data_point.data_acquisition_id IS '设备数据采集外键';
COMMENT ON COLUMN gateway_data_point.device_code IS '设备编码';
COMMENT ON COLUMN gateway_data_point.metric_name IS '指标名称';
