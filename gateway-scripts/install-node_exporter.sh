#!/bin/bash

VERSION=1.9.0
OS=linux
ARCH=amd64
FILE_NAME=node_exporter-${VERSION}.${OS}-${ARCH}

source check.sh

if [ ! -f tmp/${FILE_NAME}.tar.gz ]; then
  wget -P tmp https://github.com/prometheus/node_exporter/releases/download/v${VERSION}/${FILE_NAME}.tar.gz || exit 1
fi

tar -zxvf tmp/${FILE_NAME}.tar.gz -C tmp

if [ ! $? -eq 0 ]; then
  echo "解压 tmp/${FILE_NAME}.tar.gz 失败，删掉文件后重新运行"
  exit 1
fi

if [ ! -f $WORKDIR/node_exporter ]; then
  cp tmp/${FILE_NAME}/node_exporter $WORKDIR/node_exporter || exit 1
fi

sudo tee /etc/systemd/system/node_exporter.service <<EOF
[Unit]
Description=Node Exporter
After=network.target

[Service]
WorkingDirectory=$WORKDIR
ExecStart=$WORKDIR/node_exporter

[Install]
WantedBy=multi-user.target
EOF

chmod +x $WORKDIR/node_exporter

sudo systemctl daemon-reload && \
sudo systemctl enable node_exporter && \
sudo systemctl restart node_exporter

if [ $? -eq 0 ]; then
  echo "安装 node_exporter 成功"
fi

sudo systemctl status node_exporter
