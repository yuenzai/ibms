CREATE TABLE gateway_data_acquisition
(
    id                    INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    data_acquisition_code VARCHAR(64)        NOT NULL COMMENT '设备数据采集编码',
    payload               TEXT               NOT NULL,
    version               INT                NOT NULL COMMENT '乐观锁版本',
    created_date          BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date    BIGINT             NOT NULL COMMENT '修改时间',
    CONSTRAINT pk_gateway_data_acquisition PRIMARY KEY (id),
    CONSTRAINT uk_gateway_data_acquisition UNIQUE (data_acquisition_code)
) COMMENT '设备数据采集';

CREATE TABLE gateway_data_point
(
    id                  INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    data_acquisition_id INT                NOT NULL COMMENT '设备数据采集外键',
    device_code         VARCHAR(64)        NOT NULL COMMENT '设备编码',
    metric_name         VARCHAR(64)        NOT NULL COMMENT '指标名称',
    CONSTRAINT pk_gateway_data_point PRIMARY KEY (id),
    CONSTRAINT uk_gateway_data_point UNIQUE (device_code, metric_name)
) COMMENT '设备数据点位';
