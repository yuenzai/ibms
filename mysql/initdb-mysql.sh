#!/bin/bash

DATABASE=gateway
USERNAME=gateway
PASSWORD="CJVixCszaS+7raa/5326YJDq3xrSBXHg"

echo "初始化数据库: $DATABASE..."
mysql -u root -p$MYSQL_ROOT_PASSWORD <<-EOSQL
CREATE DATABASE $DATABASE;
CREATE USER '$USERNAME'@'%' IDENTIFIED BY '$PASSWORD';
GRANT ALL PRIVILEGES ON $DATABASE.* TO '$USERNAME'@'%';
FLUSH PRIVILEGES;
EOSQL
if [ $? -eq 0 ]; then
    echo "数据库 $DATABASE 初始化成功！"
else
    echo "数据库 $DATABASE 初始化失败！"
    exit 1
fi
