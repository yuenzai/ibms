#!/bin/bash

GATEWAY_CODE=qie

VERSION=3.2.1
OS=linux
ARCH=amd64
FILE_NAME=prometheus-${VERSION}.${OS}-${ARCH}
LISTEN_ADDRESS=0.0.0.0:9090

source check.sh

if [ ! -f tmp/${FILE_NAME}.tar.gz ]; then
  wget -P tmp https://github.com/prometheus/prometheus/releases/download/v${VERSION}/${FILE_NAME}.tar.gz || exit 1
fi

tar -zxvf tmp/${FILE_NAME}.tar.gz -C tmp

if [ ! $? -eq 0 ]; then
  echo "解压 tmp/${FILE_NAME}.tar.gz 失败，删掉文件后重新运行"
  exit 1
fi

if [ ! -f ${WORKDIR}/prometheus ]; then
  cp tmp/${FILE_NAME}/prometheus ${WORKDIR}/prometheus || exit 1
fi

if [ ! -f ${WORKDIR}/prometheus.yml ]; then
  cp prometheus.yml ${WORKDIR}/prometheus.yml || exit 1
fi

if [ ! -f ${WORKDIR}/scrape_config_file.yml ]; then
  cp scrape_config_file.yml ${WORKDIR}/scrape_config_file.yml || exit 1
fi

if [ ! -d ${WORKDIR}/prometheus-data ]; then
  mkdir -p ${WORKDIR}/prometheus-data || exit 1
fi

sudo tee /etc/systemd/system/prometheus.service <<EOF
[Unit]
Description=Prometheus
After=network.target

[Service]
WorkingDirectory=$WORKDIR
Environment="GATEWAY_CODE=$GATEWAY_CODE"
ExecStart=${WORKDIR}/prometheus \
--config.file=${WORKDIR}/prometheus.yml \
--config.auto-reload-interval=30s \
--storage.agent.path=${WORKDIR}/prometheus-data \
--enable-feature=auto-reload-config,metadata-wal-records \
--agent \
--web.listen-address=${LISTEN_ADDRESS}
[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload && \
sudo systemctl enable prometheus && \
sudo systemctl restart prometheus

if [ $? -eq 0 ]; then
  echo "安装 prometheus 成功"
fi

sudo systemctl status prometheus
