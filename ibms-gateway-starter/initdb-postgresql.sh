#!/bin/bash

GATEWAY_DB=gateway
GATEWAY_USER=gateway
GATEWAY_PASSWORD="a123451!"

echo "初始化数据库: $GATEWAY_DB..."
psql --username postgres <<-EOSQL
CREATE DATABASE $GATEWAY_DB;
\c $GATEWAY_DB;
CREATE USER $GATEWAY_USER WITH PASSWORD '$GATEWAY_PASSWORD';
CREATE SCHEMA $GATEWAY_USER AUTHORIZATION $GATEWAY_USER;
ALTER USER $GATEWAY_USER SET search_path TO $GATEWAY_USER;
EOSQL

if [ $? -eq 0 ]; then
    echo "数据库 $GATEWAY_DB 初始化成功！"
else
    echo "数据库 $GATEWAY_DB 初始化失败！"
    exit 1
fi

echo "PostgreSQL 部署完成！"
