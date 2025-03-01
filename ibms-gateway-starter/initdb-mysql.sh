#!/bin/bash

GATEWAY_DB=gateway
GATEWAY_USER=gateway
GATEWAY_PASSWORD="a123451!"

echo "初始化数据库: $GATEWAY_DB..."
mysql -u root -p$MYSQL_ROOT_PASSWORD <<-EOSQL
CREATE DATABASE $GATEWAY_DB;
CREATE USER '$GATEWAY_USER'@'%' IDENTIFIED BY '$GATEWAY_PASSWORD';
GRANT ALL PRIVILEGES ON $GATEWAY_DB.* TO '$GATEWAY_USER'@'%';
FLUSH PRIVILEGES;
EOSQL
if [ $? -eq 0 ]; then
    echo "数据库 $GATEWAY_DB 初始化成功！"
else
    echo "数据库 $GATEWAY_DB 初始化失败！"
    exit 1
fi

echo "MySQL 部署完成！"