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

ALTER TABLE dictionary
    ADD CONSTRAINT uk_dictionary UNIQUE (dict_key);

ALTER TABLE outbox_processed
    ADD CONSTRAINT uk_outbox_processed UNIQUE (event_id);
