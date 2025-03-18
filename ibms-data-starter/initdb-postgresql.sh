#!/bin/bash

DATABASE=iceberg
USERNAME=iceberg
PASSWORD="a123451!"

echo "初始化数据库: $DATABASE..."
psql --username postgres <<-EOSQL
CREATE DATABASE $DATABASE;
\c $DATABASE;
CREATE USER $USERNAME WITH PASSWORD '$PASSWORD';
CREATE SCHEMA $USERNAME AUTHORIZATION $USERNAME;
ALTER USER $USERNAME SET search_path TO $USERNAME;
EOSQL

if [ $? -eq 0 ]; then
    echo "数据库 $DATABASE 初始化成功！"
else
    echo "数据库 $DATABASE 初始化失败！"
    exit 1
fi

echo "PostgreSQL 部署完成！"
