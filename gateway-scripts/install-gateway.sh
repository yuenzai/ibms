#!/bin/bash

BACNET_IFACE=ens18

JRE_NAME=OpenJDK17U-jre_x64_linux_hotspot_17.0.14_7.tar.gz
JRE_LINK=https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.14%2B7/${JRE_NAME}
JAR_NAME=ibms-gateway-starter-0.0.1-SNAPSHOT.jar

source check.sh

if [ ! -d $WORKDIR/jdk-* ]; then
  wget -P tmp ${JRE_LINK} || exit 1
  tar -zxvf tmp/${JRE_NAME} -C $WORKDIR
fi

if [ ! -f $WORKDIR/$JAR_NAME ]; then
  read -sp "是否要重新下载网关程序？[y/n]" FLAG_DOWNLOAD
  echo
  if [ $FLAG_DOWNLOAD == "y" ]; then
    scp metadata@110.41.57.238:~/ibms-gateway.tar.gz tmp/ibms-gateway.tar.gz
    if [ ! $? -eq 0 ]; then
      echo "下载失败"
      exit 1
    fi
  fi
  tar -zxvf tmp/ibms-gateway.tar.gz -C tmp
  if [ ! $? -eq 0 ]; then
    echo "解压 tmp/ibms-gateway.tar.gz 失败，删掉文件后重新运行"
    exit 1
  fi
  cp tmp/$JAR_NAME $WORKDIR/$JAR_NAME
fi

JAVA_PATH=$(find /root/gateway/jdk-17.0.14+7-jre/bin/java)

sudo tee /etc/systemd/system/ibms-gateway.service <<EOF
[Unit]
Description=IBMS Gateway
After=network.target

[Service]
WorkingDirectory=$WORKDIR
Environment="BACNET_IFACE=$BACNET_IFACE"
ExecStart=$JAVA_PATH -jar $WORKDIR/$JAR_NAME

[Install]
WantedBy=multi-user.target
EOF

chmod +x $WORKDIR/$JAR_NAME

sudo systemctl daemon-reload && \
sudo systemctl enable ibms-gateway && \
sudo systemctl restart ibms-gateway

if [ $? -eq 0 ]; then
  echo "安装 ibms-gateway 成功"
fi

sudo systemctl status ibms-gateway
