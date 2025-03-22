#!/bin/bash

VERSION=1.9.0
OS=linux
ARCH=amd64
FILE_NAME=node_exporter-${VERSION}.${OS}-${ARCH}

if [ ! -f /tmp/${FILE_NAME}.tar.gz ]; then
  wget -P /tmp https://github.com/prometheus/node_exporter/releases/download/v${VERSION}/${FILE_NAME}.tar.gz || exit 1;
fi

tar -zxvf /tmp/${FILE_NAME}.tar.gz -C /tmp

if [ ! $? -eq 0 ]; then
  echo "解压 /tmp/${FILE_NAME}.tar.gz 失败，删掉文件后重新运行"
  exit 1
fi

if [ ! -f /usr/local/bin/node_exporter ]; then
  sudo cp /tmp/${FILE_NAME}/node_exporter /usr/local/bin/node_exporter
fi

sudo chmod +x /usr/local/bin/node_exporter

sudo tee /etc/systemd/system/node_exporter.service <<EOF
[Unit]
Description=Node Exporter
After=network.target

[Service]
ExecStart=/usr/local/bin/node_exporter

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload && \
sudo systemctl start node_exporter.service && \
sudo systemctl enable node_exporter.service

if [ ! $? -eq 0 ]; then
  echo "node_exporter install failed"
  sudo systemctl status node_exporter.service
else
  echo "node_exporter installed"
fi
