#!/bin/bash
IBMS_HOME=$(pwd)
VERSION=1.9.0
OS=linux
ARCH=amd64
FILE_NAME=node_exporter-${VERSION}.${OS}-${ARCH}

if [ ! -d tmp ]; then
  mkdir tmp
fi

if [ ! -d bin ]; then
  mkdir bin
fi

if [ ! -f tmp/${FILE_NAME}.tar.gz ]; then
  wget -P tmp https://github.com/prometheus/node_exporter/releases/download/v${VERSION}/${FILE_NAME}.tar.gz || exit 1;
fi

tar -zxvf tmp/${FILE_NAME}.tar.gz -C tmp

if [ ! -f ${IBMS_HOME}/bin/node_exporter ]; then
  cp tmp/${FILE_NAME}/node_exporter bin
fi

chmod +x bin/node_exporter

tee /etc/systemd/system/node_exporter.service <<EOF
[Unit]
Description=Node Exporter
After=network.target

[Service]
ExecStart=${IBMS_HOME}/bin/node_exporter

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload && \
systemctl start node_exporter.service && \
systemctl enable node_exporter.service && \
systemctl status node_exporter.service
