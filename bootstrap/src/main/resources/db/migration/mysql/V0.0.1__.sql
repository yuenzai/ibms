CREATE TABLE device
(
    id                 INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    version            INT                NOT NULL COMMENT '乐观锁版本',
    created_date       BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date BIGINT             NOT NULL COMMENT '修改时间',
    device_code        VARCHAR(64)        NOT NULL COMMENT '设备编码',
    network_id         VARCHAR(64)        NOT NULL COMMENT '网络ID',
    device_name        VARCHAR(255)       NOT NULL COMMENT '设备名称',
    path               VARCHAR(255)       NOT NULL COMMENT '目录路径',
    description        VARCHAR(255)       NOT NULL COMMENT '描述',
    enabled            TINYINT            NOT NULL COMMENT '是否启用',
    properties         JSON               NOT NULL COMMENT '其他属性',
    CONSTRAINT pk_device PRIMARY KEY (id)
) COMMENT '设备';

ALTER TABLE device
    ADD CONSTRAINT uk_device UNIQUE (device_code);

CREATE TABLE device_point
(
    id                 INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    created_date       BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date BIGINT             NOT NULL COMMENT '修改时间',
    device_code        VARCHAR(64)        NOT NULL COMMENT '设备编码',
    point_code         VARCHAR(64)        NOT NULL COMMENT '点位编码',
    point_name         VARCHAR(255)       NOT NULL COMMENT '点位名称',
    properties         JSON               NOT NULL COMMENT '其他属性',
    device_id          INT                NOT NULL COMMENT '设备主键',
    CONSTRAINT pk_device_point PRIMARY KEY (id)
) COMMENT '设备点位';

ALTER TABLE device_point
    ADD CONSTRAINT uk_device_point UNIQUE (device_code, point_code);

CREATE TABLE dictionary
(
    id                 INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    version            INT                NOT NULL COMMENT '乐观锁版本',
    created_date       BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date BIGINT             NOT NULL COMMENT '修改时间',
    dict_key           VARCHAR(64)        NOT NULL COMMENT '字典Key',
    dict_value         JSON               NOT NULL COMMENT '字典Value',
    CONSTRAINT pk_dictionary PRIMARY KEY (id)
) COMMENT '系统字典';

ALTER TABLE dictionary
    ADD CONSTRAINT uk_dictionary UNIQUE (dict_key);

CREATE TABLE outbox
(
    id             INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    aggregate_type VARCHAR(64)        NOT NULL COMMENT '聚合类型',
    aggregate_id   VARCHAR(64)        NOT NULL COMMENT '聚合唯一标识',
    event_id       CHAR(32)           NOT NULL COMMENT '事件唯一标识',
    event_time     BIGINT             NOT NULL COMMENT '事件时间',
    event_type     VARCHAR(64)        NOT NULL COMMENT '事件类型',
    payload        JSON               NULL COMMENT '事件内容',
    CONSTRAINT pk_outbox PRIMARY KEY (id)
) COMMENT '发件箱事件';

CREATE TABLE outbox_processed
(
    id       INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    event_id CHAR(32)           NOT NULL COMMENT '事件唯一标识',
    CONSTRAINT pk_outbox_processed PRIMARY KEY (id)
) COMMENT '发件箱事件（已处理）';

ALTER TABLE outbox_processed
    ADD CONSTRAINT uk_outbox_processed UNIQUE (event_id);
