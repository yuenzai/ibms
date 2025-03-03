#!/bin/bash

IBMS_HOME=$(pwd)

if [ -z ${IBMS_EXTERNAL_HOST} ]; then
  echo "缺少环境变量：IBMS_EXTERNAL_HOST"
  exit 1
fi

if [ -z ${GRAFANA_ADMIN_USER} ]; then
  echo "缺少环境变量：GRAFANA_ADMIN_USER"
  exit 1
fi

if [ -z ${GRAFANA_ADMIN_PASSWORD} ]; then
  echo "缺少环境变量：GRAFANA_ADMIN_PASSWORD"
  exit 1
fi

sudo tee /etc/systemd/system/ibms.service <<EOF
[Unit]
Description=Intelligent Building Management System
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
Environment="IBMS_EXTERNAL_HOST=${IBMS_EXTERNAL_HOST}"
Environment="GRAFANA_ADMIN_USER=${GRAFANA_ADMIN_USER}"
Environment="GRAFANA_ADMIN_PASSWORD=${GRAFANA_ADMIN_PASSWORD}"
WorkingDirectory=${IBMS_HOME}
ExecStart=docker compose up -d
ExecStop=docker compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload && \
sudo systemctl enable ibms.service && \
sudo systemctl restart ibms.service && \
sudo systemctl status ibms.service
