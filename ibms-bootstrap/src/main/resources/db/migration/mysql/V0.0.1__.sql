CREATE TABLE device
(
    id                 INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    device_code        VARCHAR(64)        NOT NULL COMMENT '设备编码',
    schemas_code       VARCHAR(64)        NOT NULL COMMENT '设备模型编码',
    device_name        VARCHAR(255)       NOT NULL COMMENT '设备名称',
    device             JSON               NOT NULL COMMENT '设备',
    version            INT                NOT NULL COMMENT '乐观锁版本',
    created_date       BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date BIGINT             NOT NULL COMMENT '修改时间',
    CONSTRAINT pk_device PRIMARY KEY (id)
) COMMENT '设备';

ALTER TABLE device
    ADD CONSTRAINT uk_device UNIQUE (device_code);

CREATE TABLE device_schemas
(
    id                 INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    schemas_code       VARCHAR(64)        NOT NULL COMMENT '设备模型编码',
    device_schemas     JSON               NOT NULL COMMENT '设备模型',
    version            INT                NOT NULL COMMENT '乐观锁版本',
    created_date       BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date BIGINT             NOT NULL COMMENT '修改时间',
    CONSTRAINT pk_device_schemas PRIMARY KEY (id)
) COMMENT '设备模型';

ALTER TABLE device_schemas
    ADD CONSTRAINT uk_device_schemas UNIQUE (schemas_code);

CREATE TABLE device_daq
(
    id                 INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    daq_code           VARCHAR(64)        NOT NULL COMMENT '设备数据采集编码',
    device_daq         JSON               NOT NULL COMMENT '设备数据采集',
    version            INT                NOT NULL COMMENT '乐观锁版本',
    created_date       BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date BIGINT             NOT NULL COMMENT '修改时间',
    CONSTRAINT pk_device_daq PRIMARY KEY (id)
) COMMENT '设备数据采集';

ALTER TABLE device_daq
    ADD CONSTRAINT uk_device_daq UNIQUE (daq_code);

CREATE TABLE device_gateway
(
    id                    INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    gateway_code          VARCHAR(64)        NOT NULL COMMENT '设备网关编码',
    synchronization_state VARCHAR(32)        NOT NULL COMMENT '设备网关同步状态',
    device_gateway        JSON               NOT NULL COMMENT '设备网关',
    version               INT                NOT NULL COMMENT '乐观锁版本',
    created_date          BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date    BIGINT             NOT NULL COMMENT '修改时间',
    CONSTRAINT pk_device_gateway PRIMARY KEY (id)
) COMMENT '设备网关';

ALTER TABLE device_gateway
    ADD CONSTRAINT uk_device_gateway UNIQUE (gateway_code);

CREATE TABLE rel_daq_device
(
    id        INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    daq_id    INT                NOT NULL COMMENT '设备数据采集主键',
    device_id INT                NOT NULL COMMENT '设备主键',
    CONSTRAINT pk_rel_daq_device PRIMARY KEY (id)
) COMMENT '设备数据采集和设备关联表';

CREATE TABLE outbox_event
(
    id                INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    event_id          CHAR(32)           NOT NULL COMMENT '事件唯一标识',
    event_destination VARCHAR(64)        NOT NULL COMMENT '事件目的地',
    event_key         VARCHAR(64)        NOT NULL COMMENT '事件分区键',
    event_time        BIGINT             NOT NULL COMMENT '事件时间',
    event_payload     JSON               NOT NULL COMMENT '事件内容',
    CONSTRAINT pk_outbox_event PRIMARY KEY (id)
) COMMENT '发件箱事件';

-- CREATE TABLE OUTBOX_PROCESSED
-- (
--     id       INT AUTO_INCREMENT NOT NULL COMMENT '主键',
--     event_id CHAR(32)           NOT NULL COMMENT '事件唯一标识',
--     CONSTRAINT PK_OUTBOX_PROCESSED PRIMARY KEY (id)
-- ) COMMENT '发件箱事件（已处理）';
--
-- ALTER TABLE OUTBOX_PROCESSED
--     ADD CONSTRAINT UK_OUTBOX_PROCESSED UNIQUE (event_id);

CREATE TABLE scheduling
(
    id                     INT AUTO_INCREMENT NOT NULL COMMENT '主键',
    scheduling_name        VARCHAR(64)        NOT NULL COMMENT '计划任务名称',
    scheduling_trigger     JSON               NOT NULL COMMENT '计划任务触发器',
    scheduling_task_params JSON               NOT NULL COMMENT '计划任务参数',
    description            VARCHAR(255)       NOT NULL COMMENT '描述',
    version                INT                NOT NULL COMMENT '乐观锁版本',
    created_date           BIGINT             NOT NULL COMMENT '创建时间',
    last_modified_date     BIGINT             NOT NULL COMMENT '修改时间',
    CONSTRAINT pk_scheduling PRIMARY KEY (id)
) COMMENT '计划任务';

ALTER TABLE scheduling
    ADD CONSTRAINT uk_scheduling UNIQUE (scheduling_name);
